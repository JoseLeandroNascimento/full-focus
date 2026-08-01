package com.joseleandro.fullfocus.domain.usecase

import com.joseleandro.fullfocus.data.local.database.model.PomodoroEntity
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.data.local.database.model.PomodoroWithSessions
import com.joseleandro.fullfocus.data.local.database.model.SessionEntity
import com.joseleandro.fullfocus.data.local.database.model.SessionStatus
import com.joseleandro.fullfocus.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class GetStatisticsUseCaseTest {

    private lateinit var repository: FakeStatisticRepository
    private lateinit var useCase: GetStatisticsUseCase

    @Before
    fun setup() {
        repository = FakeStatisticRepository()
        useCase = GetStatisticsUseCase(repository)
    }

    @Test
    fun `usecase should aggregate data correctly for current month`() = runBlocking {
        val today = LocalDate.now()
        val zoneId = ZoneId.systemDefault()

        val pomodoro = PomodoroEntity(id = 1, createAt = today.atStartOfDay(zoneId).toInstant().toEpochMilli())
        val sessions = listOf(
            SessionEntity(id = 1, pomodoroId = 1, duration = 60000, elapsedTime = 60000, state = PomodoroState.FOCUS, status = SessionStatus.COMPLETED, createdAt = today.atStartOfDay(zoneId).toInstant().toEpochMilli())
        )

        repository.emit(listOf(PomodoroWithSessions(pomodoro, sessions)))

        val result = useCase(today).first()

        assertEquals(1, result.dailyStreak)
        assertEquals(1, result.focusSessionsCompleted)
        assertEquals(60000L, result.totalFocusTimeToday)
    }
}

class FakeStatisticRepository : StatisticRepository {
    private val _data = MutableStateFlow<List<PomodoroWithSessions>>(emptyList())
    fun emit(list: List<PomodoroWithSessions>) { _data.value = list }
    override fun getAllPomodorosWithSessions(): Flow<List<PomodoroWithSessions>> = _data
}
