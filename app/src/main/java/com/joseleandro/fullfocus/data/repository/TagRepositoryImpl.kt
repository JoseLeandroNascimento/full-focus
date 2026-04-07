package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.datasource.TagLocalDataSource
import com.joseleandro.fullfocus.domain.data.TagDomain
import com.joseleandro.fullfocus.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow

class TagRepositoryImpl(
    private val tagLocalDataSource: TagLocalDataSource
) : TagRepository {

    override val tagsAll: Flow<List<TagDomain>>
        get() = tagLocalDataSource.tagsAll

    override suspend fun save(tag: TagDomain) {
        tagLocalDataSource.save(tag)
    }
}