package com.joseleandro.fullfocus.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.joseleandro.fullfocus.R
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroTimePreferences
import com.joseleandro.fullfocus.domain.data.PomodoroTimeUIEffect
import com.joseleandro.fullfocus.domain.repository.PomodoroSessionRepository
import com.joseleandro.fullfocus.domain.repository.PomodoroTimeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

const val ACTION_START = "ACTION_START"
const val ACTION_PAUSE = "ACTION_PAUSE"
const val ACTION_RESTART = "ACTION_RESTART"
const val ACTION_PLAY = "ACTION_PLAY"
const val ACTION_SKIP = "ACTION_SKIP"
const val ACTION_CANCEL_COMPLETED = "ACTION_CANCEL_COMPLETED"
const val ACTION_CANCEL_DISCARD = "ACTION_CANCEL_DISCARD"


class PomodoroService : Service(), KoinComponent {

    private val TAG = "PomodoroService"
    private val CHANNEL_ID = "pomodoro_channel"

    private var timerJob: Job? = null

    private val repository: PomodoroTimeRepository by inject()
    private val pomodoroSessionRepository: PomodoroSessionRepository by inject()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var observing = false
    private var observingEvents = false

    private lateinit var pausePending: PendingIntent
    private lateinit var playPending: PendingIntent
    private lateinit var restartPending: PendingIntent

    private var lastProgress = -1
    private var lastRunningState: Boolean? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        initPendingIntents()
    }

    private fun initPendingIntents() {
        val pauseIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_PAUSE
        }

        val playIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_PLAY
        }


        val restartIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_RESTART
        }

        pausePending =
            PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        playPending =
            PendingIntent.getService(this, 1, playIntent, PendingIntent.FLAG_IMMUTABLE)



        restartPending =
            PendingIntent.getService(this, 3, restartIntent, PendingIntent.FLAG_IMMUTABLE)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                1,
                createNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(1, createNotification())
        }

        if (action == null) {
            observeUpdates()
            return START_STICKY
        }

        when (action) {
            ACTION_START -> {
                scope.launch { repository.start() }
            }

            ACTION_PAUSE -> {
                scope.launch { repository.pause() }
            }

            ACTION_PLAY -> {
                scope.launch { repository.play() }
            }

            ACTION_RESTART -> {
                scope.launch { repository.restart() }
            }

            ACTION_SKIP -> {
                scope.launch { repository.skip() }
            }

            ACTION_CANCEL_COMPLETED -> {
                scope.launch { repository.cancel(completed = true) }
            }

            ACTION_CANCEL_DISCARD -> {
                scope.launch { repository.cancel(completed = false) }
            }

            else -> Log.d(TAG, "⚠️ Ação desconhecida: $action")
        }

        observeEvents()
        observeUpdates()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pomodoro Timer",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)

        }
    }

    private fun observeEvents() {
        if (observingEvents) return
        observingEvents = true

        scope.launch {
            repository.events.collect { event ->

                when (event) {

                    is PomodoroTimeUIEffect.PomodoroTimeCompleted -> {
                        pomodoroSessionRepository.completeSession()
                        showFinishedNotification()
                    }

                    is PomodoroTimeUIEffect.Start -> {
                        pomodoroSessionRepository.startSession(state = event.sessionInfo)
                    }

                    is PomodoroTimeUIEffect.UpdateTaskId -> {
                        pomodoroSessionRepository.updateActiveSessionTask(taskId = event.taskId)
                    }

                    PomodoroTimeUIEffect.Restart -> {
                        pomodoroSessionRepository.restartSession()
                    }

                    is PomodoroTimeUIEffect.Skip -> {
                        pomodoroSessionRepository.skipSession()
                    }

                    is PomodoroTimeUIEffect.Cancel -> {
                        if (event.completed) {
                            pomodoroSessionRepository.completeSession()
                            showFinishedNotification()
                        } else {
                            pomodoroSessionRepository.cancelSession()
                        }
                    }
                }
            }
        }
    }

    private fun observeUpdates() {
        if (observing) return
        observing = true

        scope.launch {
            repository.pomodoroFlow.collectLatest { state ->

                updateNotification(state)

                if (state.isRunning) {
                    startTimer()
                } else {
                    stopTimer()
                }

                if (state.startTime == 0L) {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }
            }
        }
    }

    private fun startTimer() {

        if (timerJob?.isActive == true) return

        timerJob = scope.launch {

            while (true) {

                val state = repository.pomodoroFlow.first()
                val remaining = repository.getRemaining(state)

                if (!state.isRunning) break

                if (remaining <= 0) {

                    repository.onPomodoroSessionFinished()
                    break
                }

                delay(1000)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }


    private fun showFinishedNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.round_check_circle_24)
            .setContentTitle("Pomodoro Finalizado!")
            .setContentText("Parabéns! Você completou sua sessão de foco.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setColor(0xFF4CAF50.toInt()) // Verde Premium para sucesso
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(2, notification) // ID diferente para não conflitar
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Pomodoro")
            .setContentText("Iniciando foco...")
            .setSmallIcon(R.drawable.outline_access_time_24)
            .setOngoing(true)
            .setColor(0xFFE57373.toInt())
            .setColorized(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification(state: PomodoroTimePreferences) {

        val remainingMillis = repository.getRemaining(state)
        val maxProgress = (state.duration / 1000).toInt()
        val currentProgress = (remainingMillis / 1000).toInt()

        // Otimização: Só atualiza se o segundo ou estado mudar
        if (currentProgress == lastProgress && state.isRunning == lastRunningState) {
            return
        }

        lastProgress = currentProgress
        lastRunningState = state.isRunning

        Log.d(TAG, "⏱️ Atualizando notificação: $remainingMillis ms")

        // Configuração visual Premium
        val title = if (state.isRunning) "Foco Ativo" else "Timer Pausado"
        val mainIcon =
            if (state.isRunning) R.drawable.baseline_pause_24 else R.drawable.round_play_arrow_24
        val mainActionLabel = if (state.isRunning) "Pausar" else "Continuar"
        val mainPendingIntent = if (state.isRunning) pausePending else playPending

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.outline_access_time_24)
            .setContentTitle(title)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setColor(0xFFE57373.toInt()) // Cor Coral Premium
            .setColorized(true) // Pinta o fundo da notificação
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(maxProgress, currentProgress, false) // Barra de progresso visível

        if (state.isRunning) {
            val endTime = System.currentTimeMillis() + remainingMillis
            builder.setUsesChronometer(true)
                .setChronometerCountDown(true)
                .setWhen(endTime)
                .setSubText("Produtividade Máxima")
        } else {
            val mmSs = String.format(
                Locale.getDefault(),
                "%02d:%02d",
                currentProgress / 60,
                currentProgress % 60
            )
            builder.setContentText("Sessão interrompida em: $mmSs")
                .setSubText("Pausado")
        }

        // Adiciona as ações
        builder.addAction(mainIcon, mainActionLabel, mainPendingIntent)
        builder.addAction(R.drawable.outline_restart_alt_24, "Restart", restartPending)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, builder.build())
    }
}