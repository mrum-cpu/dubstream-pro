package com.dubstream.pro

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AudioService : Service() {

    private var audioEngine: AudioEngine? = null
    private val NOTIFICATION_ID = 1001
    private val CHANNEL_ID = "dubstream_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        audioEngine = AudioEngine(this) { data ->
            processWithPython(data)
        }
        audioEngine?.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        audioEngine?.stop()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "DubStream Audio",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "جاري تسجيل الصوت للدبلجة"
            }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("DubStream Pro")
            .setContentText("جاري تسجيل ومعالجة الصوت...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun processWithPython(data: ByteArray) {
        try {
            val py = com.chaquo.python.Python.getInstance()
            val module = py.getModule("pipeline_v2")
            module.callAttr("process_audio", data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
