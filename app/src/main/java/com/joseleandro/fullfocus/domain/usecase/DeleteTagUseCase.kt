package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.domain.repository.TagRepository

class DeleteTagUseCase(
    private val tagRepository: TagRepository
) {
    suspend operator fun invoke(id: Int) {
        tagRepository.deleteById(id)
    }

}