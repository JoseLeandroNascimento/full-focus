package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.data.TagDomain
import com.joseleandro.fullfocus.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow

class GetTagFindAllUseCase(
    private val tagRepository: TagRepository
) {

    operator fun invoke(): Flow<List<TagDomain>> {
        return tagRepository.tagsAll
    }

}