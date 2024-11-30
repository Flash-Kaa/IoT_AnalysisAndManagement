package com.flasska.iotam.navigation

import kotlinx.serialization.Serializable

internal sealed interface NavRoute {
    @Serializable data object DetectorsData : NavRoute
}