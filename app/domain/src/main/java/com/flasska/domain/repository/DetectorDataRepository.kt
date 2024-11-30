package com.flasska.domain.repository

import com.flasska.domain.entity.DetectorData
import com.flasska.domain.entity.TemperatureActuatorState
import kotlinx.coroutines.flow.StateFlow

interface DetectorDataRepository {
    val state: StateFlow<List<DetectorData>>

    suspend fun updateTemperatureActuatorState(value: TemperatureActuatorState)
}