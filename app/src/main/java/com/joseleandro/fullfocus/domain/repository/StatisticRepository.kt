package com.joseleandro.fullfocus.domain.repository

import com.joseleandro.fullfocus.data.local.database.model.PomodoroWithSessions
import kotlinx.coroutines.flow.Flow

interface StatisticRepository {
    fun getAllPomodorosWithSessions(): Flow<List<PomodoroWithSessions>>
}
