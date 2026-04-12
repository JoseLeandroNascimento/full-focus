package com.joseleandro.fullfocus.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroPreferences
import com.joseleandro.fullfocus.data.local.preferences.userPreferencesDataStore
import com.joseleandro.fullfocus.data.repository.PomodoroRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

const val ACTION_START = "ACTION_START"
const val ACTION_PAUSE = "ACTION_PAUSE"
const val ACTION_RESET = "ACTION_RESET"
const val ACTION_RESUME = "ACTION_RESUME"
const val ACTION_SKIP = "ACTION_SKIP"


class PomodoroService : Service() {

    private val TAG = "PomodoroService"
    private val CHANNEL_ID = "pomodoro_channel"

    private lateinit var repository: PomodoroRepositoryImpl

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var observing = false

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "🔥 onCreate chamado")

        createNotificationChannel()

        repository = PomodoroRepositoryImpl(
            applicationContext.userPreferencesDataStore
        )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(TAG, "🚀 onStartCommand - action: ${intent?.action}")

        when (intent?.action) {
            ACTION_START -> {
                Log.d(TAG, "▶️ START")
                scope.launch { repository.start(duration = 25 * 60 * 1000L) }
            }

            ACTION_PAUSE -> {
                Log.d(TAG, "⏸️ PAUSE")
                scope.launch { repository.pause() }
            }

            ACTION_RESUME -> {
                Log.d(TAG, "▶️ RESUME")
                scope.launch { repository.resume() }
            }

            ACTION_RESET -> {
                Log.d(TAG, "🔄 RESET")
                scope.launch { repository.reset() }
            }

            else -> Log.d(TAG, "⚠️ Ação desconhecida")
        }

        Log.d(TAG, "📢 startForeground chamado")
        startForeground(1, createNotification())

        observeUpdates()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "💀 Service destruído")
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ---------------- CHANNEL ----------------

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pomodoro Timer",
                NotificationManager.IMPORTANCE_DEFAULT // 🔥 use DEFAULT
            )

            channel.description = "Notificação do Pomodoro em execução"

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)

            Log.d(TAG, "🔔 Canal criado com sucesso")
        }
    }

    // ---------------- OBSERVA ESTADO ----------------

    private fun observeUpdates() {
        if (observing) return
        observing = true

        Log.d(TAG, "👀 observeUpdates iniciado")

        scope.launch {
            repository.pomodoroFlow.collect { state ->
                Log.d(
                    TAG,
                    "📊 Estado -> running=${state.isRunning}, start=${state.startTime}, pausedAt=${state.pausedAt}"
                )

                updateNotification(state)
            }
        }
    }

    // ---------------- NOTIFICAÇÃO ----------------

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Pomodoro")
            .setContentText("Iniciando...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    private fun updateNotification(state: PomodoroPreferences) {

        val remaining = repository.getRemaining(state) / 1000

        Log.d(TAG, "⏱️ Atualizando notificação: $remaining s")

        val pauseIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_PAUSE
        }

        val resumeIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_RESUME
        }

        val resetIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_RESET
        }

        val pausePending =
            PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        val resumePending =
            PendingIntent.getService(this, 1, resumeIntent, PendingIntent.FLAG_IMMUTABLE)

        val resetPending =
            PendingIntent.getService(this, 2, resetIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Pomodoro em andamento")
            .setContentText("Tempo restante: $remaining s")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .addAction(0, "Pausar", pausePending)
            .addAction(0, "Continuar", resumePending)
            .addAction(0, "Resetar", resetPending)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
    }
}