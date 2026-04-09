package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.TaskFilterPreferencesRepository

class GetArgumentFiltersTasks(
    private val taskFilterPreferencesRepository: TaskFilterPreferencesRepository
) {

    operator fun invoke() = taskFilterPreferencesRepository.taskFilter

}