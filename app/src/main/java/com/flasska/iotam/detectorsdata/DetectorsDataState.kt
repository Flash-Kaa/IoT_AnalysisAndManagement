package com.flasska.iotam.detectorsdata

import com.flasska.domain.entity.ActuatorState
import com.flasska.domain.entity.DetectorData
import com.flasska.domain.entity.TemperatureActuatorState
import java.time.LocalDateTime

internal data class DetectorsDataState(
    val current: DetectorData = DetectorData(
        0f,
        0f,
        ActuatorState.Auto,
        TemperatureActuatorState.None,
        LocalDateTime.now()
    ),
    val history: List<DetectorData> = emptyList()
)
