package com.flasska.domain.entity

// Changing actuator from player or program
enum class ActuatorState {
    Handle, Auto;

    companion object {
        // Deserialize by name
        fun deserialize(str: String?): ActuatorState? {
            entries.forEach {
                if (it.name == str) return it
            }

            return null
        }
    }
}