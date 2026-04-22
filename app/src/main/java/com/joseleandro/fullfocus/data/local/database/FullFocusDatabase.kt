package com.joseleandro.fullfocus.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joseleandro.fullfocus.data.local.database.dao.PomodoroSessionDao
import com.joseleandro.fullfocus.data.local.database.dao.TagDao
import com.joseleandro.fullfocus.data.local.database.dao.TaskDao
import com.joseleandro.fullfocus.data.local.database.model.entity.PomodoroSessionEntity
import com.joseleandro.fullfocus.data.local.database.model.entity.TagEntity
import com.joseleandro.fullfocus.data.local.database.model.entity.TaskEntity

const val FULL_FOCUS_DATABASE_NAME = "full_focus_database"

@Database(
    entities = [
        TagEntity::class,
        TaskEntity::class,
        PomodoroSessionEntity::class
    ],
    version = 1,
)
abstract class FullFocusDatabase : RoomDatabase() {

    abstract fun tagDao(): TagDao

    abstract fun taskDao(): TaskDao

    abstract fun pomodoroSessionDao(): PomodoroSessionDao

}