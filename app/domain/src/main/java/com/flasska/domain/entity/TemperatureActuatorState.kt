package com.flasska.domain.entity

// Temperature direction
enum class TemperatureActuatorState {
    Hot, Cold, None;

    companion object {
        // Deserialize by name
        fun deserialize(str: String?): TemperatureActuatorState? {
            TemperatureActuatorState.entries.forEach {
                if (it.name == str) return it
            }

            return null
        }
    }
}