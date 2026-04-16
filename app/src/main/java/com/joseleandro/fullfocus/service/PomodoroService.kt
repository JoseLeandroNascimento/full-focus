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
import com.joseleandro.fullfocus.domain.repository.PomodoroRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

const val ACTION_START = "ACTION_START"
const val ACTION_PAUSE = "ACTION_PAUSE"
const val ACTION_RESET = "ACTION_RESET"
const val ACTION_PLAY = "ACTION_PLAY"
const val ACTION_SKIP = "ACTION_SKIP"


class PomodoroService : Service(), KoinComponent {

    private val TAG = "PomodoroService"
    private val CHANNEL_ID = "pomodoro_channel"

    private val repository: PomodoroRepository by inject()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var observing = false

    private lateinit var pausePending: PendingIntent
    private lateinit var playPending: PendingIntent
    private lateinit var resetPending: PendingIntent

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

        val resetIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_RESET
        }

        pausePending =
            PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        playPending =
            PendingIntent.getService(this, 1, playIntent, PendingIntent.FLAG_IMMUTABLE)

        resetPending =
            PendingIntent.getService(this, 2, resetIntent, PendingIntent.FLAG_IMMUTABLE)
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

            ACTION_RESET -> {
                scope.launch { repository.reset() }
            }

            ACTION_SKIP -> {
                scope.launch { repository.skip() }
            }

            else -> Log.d(TAG, "⚠️ Ação desconhecida: $action")
        }

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


    private fun observeUpdates() {
        if (observing) return
        observing = true

        scope.launch {
            repository.pomodoroFlow.collectLatest { state ->

                if (state.isRunning) {

                    var remaining = repository.getRemaining(state)
                    while (remaining > 0) {
                        remaining = repository.getRemaining(state)
                        updateNotification(state)
                        delay(1000)
                    }

                    showFinishedNotification()
                    repository.finishedSessionPomodoro()

                } else {
                    updateNotification(state)

                    if (state.startTime == 0L) {
                        stopForeground(STOP_FOREGROUND_REMOVE)
//                        stopSelf()
                    }
                }
            }
        }
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

    // ---------------- NOTIFICAÇÃO ----------------

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
        builder.addAction(R.drawable.outline_restart_alt_24, "Resetar", resetPending)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, builder.build())
    }
}