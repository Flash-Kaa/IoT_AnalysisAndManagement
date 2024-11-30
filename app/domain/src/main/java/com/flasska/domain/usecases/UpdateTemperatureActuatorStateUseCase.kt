package com.flasska.domain.usecases

import com.flasska.domain.entity.TemperatureActuatorState
import com.flasska.domain.repository.DetectorDataRepository

class UpdateTemperatureActuatorStateUseCase(
    private val repository: DetectorDataRepository
) {
    suspend operator fun invoke(value: TemperatureActuatorState) =
        repository.updateTemperatureActuatorState(value)
}