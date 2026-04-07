package com.joseleandro.fullfocus.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.joseleandro.fullfocus.data.local.database.model.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT * FROM tag_table")
    fun getAll(): Flow<List<TagEntity>>

    @Insert
    suspend fun insert(tag: TagEntity)

    @Query(
        value = """
        UPDATE tag_table
        SET name = :name,
            color = :color,
            updated_at = :updatedAt
        WHERE id = :id
    """
    )
    suspend fun update(
        id: Int,
        name: String,
        color: Long,
        updatedAt: Long
    )

}