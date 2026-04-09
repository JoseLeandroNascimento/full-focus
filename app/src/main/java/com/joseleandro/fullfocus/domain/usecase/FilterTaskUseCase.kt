package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.TaskFilterPreferencesRepository

class FilterTaskUseCase(
    private val taskFilterPreferencesRepository: TaskFilterPreferencesRepository
) {

    suspend operator fun invoke(tagId: Int?) {
        taskFilterPreferencesRepository.updateTaskFilter(tagId)
    }
}