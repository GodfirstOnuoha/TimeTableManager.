package com.peacecodes.timetablemanager.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.peacecodes.timetablemanager.util.Util
import java.util.*

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationUtils = NotificationUtils(context)
        val notification = notificationUtils.getNotificationBuilder().build()
        notificationUtils.getManager().notify(150, notification)

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                context.startService(Intent(context, NotificationService::class.java))
                Util.displayShortMessage(context, "Restarting Scheduled Alarms")
            }
            Util.ACTION_SCHEDULE_ALARM -> {
                if (alarmIsToday(intent)) {
                    startAlarmService(context, intent)
                }
            }
            Util.ACTION_DISMISS_ALARM -> {
                context.stopService(Intent(context, NotificationService::class.java))
                Util.displayShortMessage(context, "Notification dismissed")
            }
        }
    }

    private fun alarmIsToday(intent: Intent): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            intent.getIntExtra(Util.SCHEDULE_DAY, 0) + 2 -> return true
        }
        return false
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, NotificationService::class.java)
        serviceIntent.putExtra(Util.SCHEDULE_TITLE, intent.getStringExtra(Util.SCHEDULE_TITLE))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
        Util.displayShortMessage(context, "Alarm Schedule Set.")
    }
}