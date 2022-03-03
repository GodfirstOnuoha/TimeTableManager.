package com.peacecodes.timetablemanager.notification

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.activities.MainActivity
import com.peacecodes.timetablemanager.util.Util

class NotificationService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val notificationPendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val dismissIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = Util.ACTION_DISMISS_ALARM
            putExtra(Util.SCHEDULE_ID, intent.getStringExtra(Util.SCHEDULE_ID))
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            this,
            intent.getIntExtra(Util.SCHEDULE_ID, 0),
            dismissIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val notification = NotificationCompat.Builder(this, Util.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Schedule Reminder")
            .setContentText(intent.getStringExtra(Util.SCHEDULE_TITLE))
            .setSmallIcon(R.drawable.alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            /*.setStyle(NotificationCompat.BigTextStyle()
                    .bigText(intent.getStringExtra(Constants.SCHEDULE_TITLE))
                    .setBigContentTitle("Schedule Reminder"))*/
            .addAction(R.drawable.delete, "Dismiss", dismissPendingIntent)
            .build()

        mediaPlayer.start()
        mediaPlayer.isLooping = true

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}