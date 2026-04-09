package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.UserPreferencesRepository

class FilterTaskUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    suspend operator fun invoke(tagId: Int?) {
        userPreferencesRepository.updateTaskFilter(tagId)
    }
}