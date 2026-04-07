package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.domain.data.TagDomain
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    val tagsAll: Flow<List<TagDomain>>

    suspend fun save(tag: TagDomain)

}