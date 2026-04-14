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
import com.joseleandro.fullfocus.data.local.preferences.data.PomodoroCurrentPreferences
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
const val ACTION_RESUME = "ACTION_RESUME"
const val ACTION_SKIP = "ACTION_SKIP"


class PomodoroService : Service(), KoinComponent {

    private val TAG = "PomodoroService"
    private val CHANNEL_ID = "pomodoro_channel"

    private val repository: PomodoroRepository by inject()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var observing = false

    // Otimização: Cache de PendingIntents e controle de atualização
    private lateinit var pausePending: PendingIntent
    private lateinit var resumePending: PendingIntent
    private lateinit var resetPending: PendingIntent

    private var lastProgress = -1
    private var lastRunningState: Boolean? = null

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "🔥 onCreate chamado")

        createNotificationChannel()
        initPendingIntents()
    }

    private fun initPendingIntents() {
        val pauseIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_PAUSE
        }

        val resumeIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_RESUME
        }

        val resetIntent = Intent(this, PomodoroService::class.java).apply {
            action = ACTION_RESET
        }

        pausePending =
            PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        resumePending =
            PendingIntent.getService(this, 1, resumeIntent, PendingIntent.FLAG_IMMUTABLE)

        resetPending =
            PendingIntent.getService(this, 2, resetIntent, PendingIntent.FLAG_IMMUTABLE)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

        Log.d(TAG, "📩 onStartCommand: $action")

        // Garantir que startForeground seja chamado imediatamente para evitar ForegroundServiceDidNotStartInTimeException
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
            // Se o sistema recriou o service sem intent, verificamos se deve rodar
            observeUpdates()
            return START_STICKY
        }

        when (action) {
            ACTION_START -> {
                Log.d(TAG, "▶️ START")
                scope.launch {
                    repository.start(duration = 25 * 60 * 1000L)
                }
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

            ACTION_SKIP -> {
                Log.d(TAG, "⏭️ SKIP")
                // Adicione a lógica de skip se o repositório suportar
            }

            else -> Log.d(TAG, "⚠️ Ação desconhecida: $action")
        }

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
                NotificationManager.IMPORTANCE_LOW // Usar LOW para não interromper o usuário a cada bip
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
            repository.pomodoroFlow.collectLatest { state ->
                if (state.isRunning) {
                    while (true) {
                        val remaining = repository.getRemaining(state)
                        
                        if (remaining <= 0) {
                            Log.d(TAG, "✅ Tempo esgotado!")
                            showFinishedNotification()
                            repository.reset() // Isso disparará o 'else' abaixo na próxima emissão
                            break
                        }
                        
                        updateNotification(state)
                        delay(1000)
                    }
                } else {
                    updateNotification(state)
                    
                    // Se o timer foi resetado (manualmente ou pelo fim do tempo), paramos o service
                    if (state.startTime == 0L) {
                        Log.d(TAG, "⏹️ Service parado (Reset ou Fim)")
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()
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

    private fun updateNotification(state: PomodoroCurrentPreferences) {

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
        val mainIcon = if (state.isRunning) R.drawable.baseline_pause_24 else R.drawable.round_play_arrow_24
        val mainActionLabel = if (state.isRunning) "Pausar" else "Continuar"
        val mainPendingIntent = if (state.isRunning) pausePending else resumePending

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
            val mmSs = String.format(Locale.getDefault(), "%02d:%02d", currentProgress / 60, currentProgress % 60)
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