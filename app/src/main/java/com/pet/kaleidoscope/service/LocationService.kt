package com.pet.kaleidoscope.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.pet.kaleidoscope.R
import android.app.PendingIntent
import com.pet.kaleidoscope.ui.main.MainActivity


/**
 * @author Dmitry Borodin on 3/16/19.
 */
class LocationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_TRACKING -> {
                startForeground()
            }
            STOP_TRACKING -> {
                stopForeground()
                stopSelf()
            }
        }
        return START_NOT_STICKY //TODO support sticky
    }

    override fun onBind(intent: Intent?): IBinder? { return null
    }

    private fun startForeground() {
        createNotificationChannel()
        val contentIntent = PendingIntent.getActivity(this, 0,
            Intent(this, MainActivity::class.java), 0)
        val notification = NotificationCompat.Builder(this, CHANNEL_SERVICE_NOTOFICATION)
            .setContentText(getString(R.string.notification_tracking_content))
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentIntent(contentIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        startForeground(1, notification)
    }

    private fun stopForeground() {
        stopForeground(true)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_tracking_name)
            val descriptionText = getString(R.string.notification_channel_tracking_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_SERVICE_NOTOFICATION, name, importance)
            channel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val START_TRACKING = "START_TRACKING"
        const val STOP_TRACKING = "STOP_TRACKING"
        const val CHANNEL_SERVICE_NOTOFICATION = "CHANNEL_SERVICE_NOTOFICATION"
    }
}