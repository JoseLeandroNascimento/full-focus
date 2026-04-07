package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.domain.data.TagDomain
import kotlinx.coroutines.flow.Flow

interface TagLocalDataSource {

    val tagsAll: Flow<List<TagDomain>>

    suspend fun save(tag: TagDomain)

}