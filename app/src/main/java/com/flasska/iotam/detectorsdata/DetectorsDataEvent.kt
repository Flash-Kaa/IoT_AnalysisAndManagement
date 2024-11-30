package com.flasska.iotam.detectorsdata

import com.flasska.domain.entity.TemperatureActuatorState

internal sealed interface DetectorsDataEvent {
    data class ChangeTemperatureActuatorState(
        val state: TemperatureActuatorState
    ): DetectorsDataEvent
}