package com.peacecodes.timetablemanager.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.adapters.ViewPagerAdapter
import com.peacecodes.timetablemanager.databinding.ActivityHomeBinding
import com.peacecodes.timetablemanager.db.TimeTableDbHelper
import com.peacecodes.timetablemanager.models.TimeTable
import com.peacecodes.timetablemanager.notification.NotificationReceiver
import com.peacecodes.timetablemanager.util.Util
import java.util.*
import kotlin.random.Random

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var tabTitle =
        arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    private lateinit var dbHelper: TimeTableDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = TimeTableDbHelper(this)

        binding.apply {
            // connecting the viewpager and the adapter class
            viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            //initializing and setting the title of the fragments
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitle[position]
            }.attach()
        }
        addSchedules()

    }

    private fun addSchedules() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.edit_dialog, null)
        builder.setView(view)

        val selectDay = view.findViewById<TextInputLayout>(R.id.select_day_layout)
        val items =
            listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val adapter = ArrayAdapter(this@Home, R.layout.list_item, items)
        (selectDay.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val saveBtn = view.findViewById<Button>(R.id.save_btn)
        val code = view.findViewById<TextInputEditText>(R.id.course_code)
        val title = view.findViewById<TextInputEditText>(R.id.course_title)
        val startTime = view.findViewById<TextView>(R.id.start_time_text)
        val endTime = view.findViewById<TextView>(R.id.end_time_text)

        var startHour = 8
        var startMinute = 30
        var currentStartTime = "${Util.getMainHour(startHour)}:${Util.getMainMinutes(startMinute)} ${Util.getAmOrPm(startHour)}"
        startTime.text = currentStartTime

        startTime.setOnClickListener {
            val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                startHour = hourOfDay
                startMinute = minute
                currentStartTime =
                    "${Util.getMainHour(hourOfDay)}:${Util.getMainMinutes(minute)} ${Util.getAmOrPm(
                        hourOfDay
                    )}"
                startTime.text = currentStartTime
            }
            TimePickerDialog(this, timeListener, startHour, startMinute, false).show()
        }

        var endHour = 10
        var endMinute = 30
        var currentEndTime = "${Util.getMainHour(endHour)}:${Util.getMainMinutes(endMinute)} ${Util.getAmOrPm(endHour)}"
        endTime.text = currentEndTime

        endTime.setOnClickListener {
            val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                endHour = hourOfDay
                endMinute = minute
                currentEndTime =
                    "${Util.getMainHour(hourOfDay)}:${Util.getMainMinutes(minute)} ${Util.getAmOrPm(
                        hourOfDay
                    )}"
                endTime.text = currentEndTime
            }
            TimePickerDialog(this, timeListener, endHour, endMinute, false).show()
        }

        val alertDialog = builder.create()
        saveBtn.setOnClickListener {
            if (TextUtils.isEmpty(selectDay.editText?.text) ||
                TextUtils.isEmpty(code.text) ||
                TextUtils.isEmpty(title.text ) ||
                TextUtils.isEmpty(startTime.text) ||
                TextUtils.isEmpty(endTime.text)
            ) {
                Toast.makeText(this, "Please fill out all the fields!", Toast.LENGTH_SHORT).show()
            } else {
                val timeTable = TimeTable(
                    day = selectDay.editText?.text.toString(),
                    code = code.text.toString(),
                    title = title.text.toString(),
                    start = startTime.text.toString(),
                    end = endTime.text.toString()
                )
                val result = dbHelper.insertTimeTable(timeTable)
                if (result) {
                    val alarmId: Int = Random.nextInt(Int.MAX_VALUE)
                    val alarmTitle = "It's time for your \"${timeTable.code}\" (${timeTable.start} to ${timeTable.end})"
                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val alarmIntent = Intent(this, NotificationReceiver::class.java).apply {
                        action = Util.ACTION_SCHEDULE_ALARM
                        putExtra(Util.SCHEDULE_ID, alarmId)
                        putExtra(Util.SCHEDULE_DAY, Util.getDayNumber(timeTable.day))
                        putExtra(Util.SCHEDULE_TITLE, alarmTitle)
                    }
                    val alarmPendingIntent = PendingIntent.getBroadcast(this, alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.DAY_OF_WEEK, Util.getDayNumber(timeTable.day))
                        set(Calendar.HOUR_OF_DAY, startHour)
                        set(Calendar.MINUTE, startMinute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    // if alarm time has already passed, increment day by 1
                    if (calendar.timeInMillis <= System.currentTimeMillis()) {
                        calendar.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH + 1)
                    }
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 7 * 24 * 60 * 60 * 1000, alarmPendingIntent)

                    Toast.makeText(this, "Schedule added successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Home::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Unable to add Schedules!", Toast.LENGTH_SHORT).show()
                }

                alertDialog.dismiss()
                title.setText("")
                startTime.text = ""
                endTime.text = ""
                code.setText("")
                selectDay.editText?.setText("")
            }
        }

        binding.floatingActionButton.setOnClickListener {
            alertDialog.show()
        }
    }
}

