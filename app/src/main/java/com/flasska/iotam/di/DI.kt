package com.flasska.iotam.di

import com.flasska.data.DetectorDataRepositoryImpl
import com.flasska.domain.repository.DetectorDataRepository
import com.flasska.domain.usecases.GetDetectorDataUseCase
import com.flasska.domain.usecases.UpdateTemperatureActuatorStateUseCase

object DI {
    private val repository: DetectorDataRepository = DetectorDataRepositoryImpl()

    val getDetectorDataUseCase = GetDetectorDataUseCase(repository)
    val updateTemperatureActuatorStateUseCase = UpdateTemperatureActuatorStateUseCase(repository)
}