package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.domain.data.TagDomain
import com.joseleandro.fullfocus.domain.data.TagWithTasksDetailsDomain
import kotlinx.coroutines.flow.Flow

interface TagLocalDataSource {

    val tagsAll: Flow<List<TagDomain>>

    val tagsAllWithDetailsCount: Flow<List<TagWithTasksDetailsDomain>>

    suspend fun save(tag: TagDomain)

    suspend fun deleteById(id: Int)

}