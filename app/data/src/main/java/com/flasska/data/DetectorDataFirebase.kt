package com.flasska.data

import kotlinx.serialization.Serializable

@Serializable
data class DetectorDataFirebase(
    val insideTemperature: Float,
    val outsideTemperature: Float,
    val actuatorState: String,
    val temperatureActuatorState: String,
    val time: Long
) {
    constructor() : this(0f, 0f, "", "", 0L)
}
