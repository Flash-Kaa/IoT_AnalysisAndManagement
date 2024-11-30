package com.flasska.iotam.detectorsdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.flasska.domain.entity.DetectorData
import com.flasska.domain.usecases.GetDetectorDataUseCase
import com.flasska.domain.usecases.UpdateTemperatureActuatorStateUseCase
import com.flasska.iotam.di.DI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

internal class DetectorsDataViewModel(
    private val updateTemperatureActuatorStateUseCase: UpdateTemperatureActuatorStateUseCase,
    private val getDetectorDataUseCase: GetDetectorDataUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(DetectorsDataState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getDetectorDataUseCase().collectLatest { dd ->
                val last = dd.lastOrNull()?.let { last ->
                    DetectorData(
                        insideTemperature = last.insideTemperature,
                        outsideTemperature = last.outsideTemperature,
                        actuatorState = last.actuatorState,
                        temperatureActuatorState = last.temperatureActuatorState,
                        dateTime = LocalDateTime.now()
                    )
                }

                _state.update {
                    it.copy(
                        current = last ?: it.current,
                        history = dd
                    )
                }
            }
        }
    }

    fun onEvent(event: DetectorsDataEvent) {
        when (event) {
            is DetectorsDataEvent.ChangeTemperatureActuatorState -> {
                _state.update {
                    it.copy(
                        current = it.current.copy(
                            temperatureActuatorState = event.state
                        )
                    )
                }

                viewModelScope.launch(Dispatchers.IO) {
                    updateTemperatureActuatorStateUseCase(event.state)
                }
            }
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetectorsDataViewModel(
                updateTemperatureActuatorStateUseCase = DI.updateTemperatureActuatorStateUseCase,
                getDetectorDataUseCase = DI.getDetectorDataUseCase
            ) as T
        }
    }
}