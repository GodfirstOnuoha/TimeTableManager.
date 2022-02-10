package com.peacecodes.timetablemanager.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.fragments.ResultActivity
import java.util.*

class NotificationService : IntentService("NotificationService") {

    private lateinit var notification: Notification
    private val notificationId: Int = 1000
    var mCursor: Cursor? = null

    @SuppressLint("NewApi")
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val context = this.applicationContext
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
//           notificationChannel.lightColor = color.parseColor()
//           notificationChannel.
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val CHANNEL_NAME = "TimeTable Manager"
        const val CHANNEL_ID = "com.peacecodes.timetablemanager.notification.CHANNEL_ID"
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onHandleIntent(intent: Intent?) {
        createChannel()

        var timestamp: Long = 0
        if (intent != null && intent.extras != null) {
            timestamp = intent.extras!!.getLong("timestamp")
        }
        if (timestamp > 0) {
            val context = this.applicationContext
            var notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notifyIntent = Intent(this, ResultActivity::class.java)
            val title = "TimeTable Manager"
            val message = "Your Schedules for today"
            notifyIntent.putExtra("title", title)
            notifyIntent.putExtra("message", message)
            notifyIntent.putExtra("notification", true)
            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp


            val pendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val res = this.resources
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = Notification.Builder(this, CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.calendar)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(Notification.BigTextStyle())
                    .setContentText(message).build()
            } else
                notification = Notification.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.calendar)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(uri)
                    .setStyle(Notification.BigTextStyle())
                    .setContentText(message).build()
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(notificationId, notification)
        }
    }
}