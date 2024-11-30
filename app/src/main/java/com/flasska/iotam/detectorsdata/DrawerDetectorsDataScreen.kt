package com.flasska.iotam.detectorsdata

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun DrawerDetectorsScreen() {
    val viewModel: DetectorsDataViewModel = viewModel(
        factory = DetectorsDataViewModel.Factory()
    )

    val state by viewModel.state.collectAsState()

    DetectorsScreen(state, viewModel::onEvent)
}