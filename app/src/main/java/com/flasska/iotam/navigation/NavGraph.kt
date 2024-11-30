package com.flasska.iotam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flasska.iotam.detectorsdata.DrawerDetectorsScreen

@Composable
internal fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavRoute.DetectorsData
    ) {
        composable<NavRoute.DetectorsData> {
            DrawerDetectorsScreen()
        }
    }
}