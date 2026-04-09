package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.UserPreferencesRepository

class GetArgumentFiltersTasks(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    operator fun invoke() = userPreferencesRepository.taskFilter

}