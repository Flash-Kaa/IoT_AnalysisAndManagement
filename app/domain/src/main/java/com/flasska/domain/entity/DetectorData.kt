package com.flasska.domain.entity

import java.time.LocalDateTime


data class DetectorData(
    val insideTemperature: Float,
    val outsideTemperature: Float,
    val actuatorState: ActuatorState,
    val temperatureActuatorState: TemperatureActuatorState,
    val dateTime: LocalDateTime
)
