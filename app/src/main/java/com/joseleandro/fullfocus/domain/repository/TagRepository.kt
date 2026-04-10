package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.domain.data.TagDomain
import com.joseleandro.fullfocus.domain.data.TagWithTasksDetailsDomain
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    val tagsAll: Flow<List<TagDomain>>

    val tagsAllWithTasksCount: Flow<List<TagWithTasksDetailsDomain>>

    suspend fun save(tag: TagDomain)

    suspend fun deleteById(id: Int)

}