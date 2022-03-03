package com.peacecodes.timetablemanager.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.activities.MainActivity
import com.peacecodes.timetablemanager.util.Util

class NotificationUtils(base: Context) : ContextWrapper(base) {

    private var manager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val channel = NotificationChannel(
            Util.NOTIFICATION_CHANNEL_ID,
            "TimeTable Schedules",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableVibration(true)
        getManager().createNotificationChannel(channel)
    }

    fun getManager(): NotificationManager {
        if (manager == null) manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return manager as NotificationManager
    }

    fun getNotificationBuilder(): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        return NotificationCompat.Builder(applicationContext, Util.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Schedule Reminder!")
            .setContentText("Your Alarm is working.")
            .setSmallIcon(R.drawable.alarm)
            .setColor(Color.YELLOW)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
    }

//    fun setNotification(timeInMilliSeconds: Long, activity: Activity) {
//
//        if (timeInMilliSeconds > 0) {
//            val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
//            val alarmIntent = Intent(activity.applicationContext, NotificationReceiver::class.java)
//
//            alarmIntent.putExtra("reason", "notification")
//            alarmIntent.putExtra("timestamp", timeInMilliSeconds)
//
//            val calendar = Calendar.getInstance()
//            calendar.timeInMillis = timeInMilliSeconds
//
//            val pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
//
//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//
//        }
//    }
}