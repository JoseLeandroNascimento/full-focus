package com.joseleandro.fullfocus.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joseleandro.fullfocus.data.local.database.dao.PomodoroDao
import com.joseleandro.fullfocus.data.local.database.dao.SessionDao
import com.joseleandro.fullfocus.data.local.database.model.PomodoroEntity
import com.joseleandro.fullfocus.data.local.database.model.SessionEntity

const val FULL_FOCUS_DATABASE = "FULL_FOCUS_DATABASE"

@Database(
    entities = [
        PomodoroEntity::class,
        SessionEntity::class
    ],
    version = 3
)
abstract class FullFocusDataBase : RoomDatabase() {

    abstract fun pomodoroDao(): PomodoroDao

    abstract fun sessionDao(): SessionDao
}