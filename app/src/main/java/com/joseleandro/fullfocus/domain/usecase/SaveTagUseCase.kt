package com.joseleandro.fullfocus.domain.usecase

import androidx.compose.ui.graphics.Color
import com.joseleandro.fullfocus.domain.data.TagDomain
import com.joseleandro.fullfocus.domain.repository.TagRepository

class SaveTagUseCase(
    private val tagRepository: TagRepository
) {

    suspend operator fun invoke(id: Int = 0, name: String, color: Color) {

        val newTag = TagDomain(
            id = id,
            name = name,
            color = color
        )

        tagRepository.save(
            newTag
        )
    }
}