package com.flasska.domain.usecases

import com.flasska.domain.repository.DetectorDataRepository

class GetDetectorDataUseCase(
    private val repository: DetectorDataRepository
){
    operator fun invoke() = repository.state
}