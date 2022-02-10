package com.peacecodes.timetablemanager.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationReceiver: BroadcastReceiver() {

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onReceive(context: Context, intent: Intent?) {
        context.startService(Intent(context, NotificationService::class.java))
    }
}