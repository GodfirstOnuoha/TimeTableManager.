package com.peacecodes.timetablemanager.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

object Util {
    const val SCHEDULE_ID = "schedule_id"
    const val SCHEDULE_DAY = "schedule_day"
    const val SCHEDULE_TITLE = "schedule_title"
    const val ACTION_SCHEDULE_ALARM = "action.SCHEDULE_ALARM"
    const val ACTION_DISMISS_ALARM = "action.DISMISS_ALARM"
    const val NOTIFICATION_CHANNEL_ID = "TIMETABLE_CHANNEL"

    fun displayShortMessage(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun displayLongMessage(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    fun getDayNumber(day: String): Int {
        var value = 1
        when (day) {
            "Sunday" -> value = 1
            "Monday" -> value = 2
            "Tuesday" -> value = 3
            "Wednesday" -> value = 4
            "Thursday" -> value = 5
            "Friday" -> value = 6
            "Saturday" -> value = 7
        }
        return value
    }

    fun getMainDay(day: Int): String {
        var mainDay = ""
        when (day) {
            1 -> mainDay = "Sunday"
            2 -> mainDay = "Monday"
            3 -> mainDay = "Tuesday"
            4 -> mainDay = "Wednesday"
            5 -> mainDay = "Thursday"
            6 -> mainDay = "Friday"
            7 -> mainDay = "Saturday"
        }
        return mainDay
    }

    fun getMainMinutes(min: Int): String {
        return if (min < 10) {
            "0$min"
        } else {
            min.toString()
        }
    }

    fun getMainHour(hour: Int): String {
        return if (hour > 12) {
            (hour - 12).toString()
        } else {
            if (hour == 0) {
                "12"
            } else {
                hour.toString()
            }
        }
    }

    fun getAmOrPm(hour: Int): String {
        return if (hour >= 12) {
            "PM"
        } else {
            "AM"
        }
    }
}