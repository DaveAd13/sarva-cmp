package com.sarva.fitness.domain.usecase

import com.sarva.fitness.domain.repository.FitnessRepository

class CheckHealthPermissionsUseCase(
    private val fitnessRepository: FitnessRepository
) {
    suspend operator fun invoke(): Boolean {
        return fitnessRepository.hasPermissions()
    }
}