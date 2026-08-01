package com.joseleandro.fullfocus.data.repository

import com.joseleandro.fullfocus.data.local.database.dao.PomodoroDao
import com.joseleandro.fullfocus.data.local.database.model.PomodoroWithSessions
import com.joseleandro.fullfocus.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow

class StatisticRepositoryImpl(
    private val pomodoroDao: PomodoroDao
) : StatisticRepository {
    override fun getAllPomodorosWithSessions(): Flow<List<PomodoroWithSessions>> {
        return pomodoroDao.getAllPomodorosWithSessions()
    }
}
