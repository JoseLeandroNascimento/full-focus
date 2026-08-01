package com.joseleandro.fullfocus.data.datasource

import com.joseleandro.fullfocus.data.local.database.dao.PomodoroDao
import com.joseleandro.fullfocus.data.local.database.dao.SessionDao
import com.joseleandro.fullfocus.data.local.database.model.PomodoroEntity
import com.joseleandro.fullfocus.data.local.database.model.PomodoroState
import com.joseleandro.fullfocus.data.local.database.model.PomodoroWithSessions
import com.joseleandro.fullfocus.data.local.database.model.SessionEntity
import com.joseleandro.fullfocus.data.local.database.model.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class StatisticPomodoroDataSourceImplTest {

    private lateinit var pomodoroDao: FakePomodoroDao
    private lateinit var dataSource: StatisticPomodoroDataSourceImpl

    @Before
    fun setup() {
        pomodoroDao = FakePomodoroDao()
        dataSource = StatisticPomodoroDataSourceImpl(pomodoroDao, FakeSessionDao())
    }

    @Test
    fun `statistic should return correct focus metrics including cancelled with time`() = runBlocking {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val zoneId = ZoneId.systemDefault()

        val pomodoroToday = PomodoroEntity(id = 1, createAt = today.atStartOfDay(zoneId).toInstant().toEpochMilli())
        val pomodoroYesterday = PomodoroEntity(id = 2, createAt = yesterday.atStartOfDay(zoneId).toInstant().toEpochMilli())

        val sessions = listOf(
            // Today: Completed focus session
            SessionEntity(id = 1, pomodoroId = 1, duration = 25 * 60 * 1000, elapsedTime = 25 * 60 * 1000, state = PomodoroState.FOCUS, status = SessionStatus.COMPLETED),
            // Yesterday: Cancelled focus session but with 10 minutes of work
            SessionEntity(id = 2, pomodoroId = 2, duration = 25 * 60 * 1000, elapsedTime = 10 * 60 * 1000, state = PomodoroState.FOCUS, status = SessionStatus.CANCEL)
        )

        val data = listOf(
            PomodoroWithSessions(pomodoroToday, listOf(sessions[0])),
            PomodoroWithSessions(pomodoroYesterday, listOf(sessions[1]))
        )

        pomodoroDao.emitWithSessions(data)

        val result = dataSource.statistic.first()

        // Streak should be 2 because yesterday has elapsed time
        assertEquals(2, result.dailyStreak)
        // Only 1 session is COMPLETED
        assertEquals(1, result.focusSessionsCompleted)
        // Total time today should be 25m
        assertEquals(25 * 60 * 1000L, result.totalFocusTimeToday)
        // Total time month should be 25m + 10m = 35m
        assertEquals(35 * 60 * 1000L, result.totalFocusTimeMonth)
    }

    @Test
    fun `statistic should return 0 when no focus sessions exist`() = runBlocking {
        pomodoroDao.emitWithSessions(emptyList())
        val result = dataSource.statistic.first()
        assertEquals(0, result.dailyStreak)
        assertEquals(0, result.focusSessionsCompleted)
        assertEquals(0L, result.totalFocusTimeToday)
    }
}

class FakePomodoroDao : PomodoroDao {
    private val _pomodoros = MutableStateFlow<List<PomodoroEntity>>(emptyList())
    private val _pomodorosWithSessions = MutableStateFlow<List<PomodoroWithSessions>>(emptyList())
    
    fun emit(list: List<PomodoroEntity>) {
        _pomodoros.value = list
    }

    fun emitWithSessions(list: List<PomodoroWithSessions>) {
        _pomodorosWithSessions.value = list
    }

    override fun getPomodoroActive(): Flow<PomodoroEntity?> = MutableStateFlow(null)
    override fun getPomodorosCompleted(): Flow<List<PomodoroEntity>> = _pomodoros
    override fun getAllPomodorosWithSessions(): Flow<List<PomodoroWithSessions>> = _pomodorosWithSessions
    override suspend fun save(data: PomodoroEntity): Long = 0
    override suspend fun update(data: PomodoroEntity): Int = 0
    override suspend fun deleteById(id: Long) {}
}

class FakeSessionDao : SessionDao {
    override fun getSessionCurrent(): Flow<SessionEntity?> = MutableStateFlow(null)
    override suspend fun save(data: SessionEntity): Long = 0L
    override suspend fun update(data: SessionEntity): Int = 0
    override suspend fun getLastSessionByPomodoroId(pomodoroId: Long): SessionEntity? = null
    override suspend fun getFocusCountByPomodoroId(pomodoroId: Long): Int = 0
    override fun getFocusCountByPomodoroIdFlow(pomodoroId: Long): Flow<Int> = MutableStateFlow(0)
    override fun getCompletedPomodoroCountFlow(pomodoroId: Long): Flow<Int> = MutableStateFlow(0)
}
