package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.database.dao.TagDao
import com.joseleandro.fullfocus.data.local.database.mapper.toDomain
import com.joseleandro.fullfocus.data.local.database.mapper.toEntity
import com.joseleandro.fullfocus.data.local.database.model.TagEntity
import com.joseleandro.fullfocus.domain.data.TagDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TagLocalDataSourceImpl(
    private val tagDao: TagDao
) : TagLocalDataSource {

    override val tagsAll: Flow<List<TagDomain>>
        get() = tagDao.getAll().map { it.toDomain() }


    override suspend fun save(tag: TagDomain) {

        val data: TagEntity = tag.toEntity()

        if (tag.id == 0) {
            tagDao.insert(data)
        } else {
            tagDao.update(
                id = data.id,
                name = data.name,
                color = data.color,
                updatedAt = System.currentTimeMillis()
            )
        }
    }
}