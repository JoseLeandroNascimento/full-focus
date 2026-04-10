package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.TagRepository

class GetTagsWithDetailsUseCase(
    private val tagRepository: TagRepository
) {

    operator fun invoke() = tagRepository.tagsAllWithTasksCount

}