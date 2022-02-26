package com.peacecodes.timetablemanager.activities

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
        val startTime = view.findViewById<TextInputEditText>(R.id.start_time)
        val endTime = view.findViewById<TextInputEditText>(R.id.end_time)

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
                    Toast.makeText(this, "Schedule added successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Home::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Unable to add Schedules!", Toast.LENGTH_SHORT).show()
                }

                alertDialog.dismiss()
                title.setText("")
                startTime.setText("")
                endTime.setText("")
                code.setText("")
                selectDay.editText?.setText("")
            }
        }

        binding.floatingActionButton.setOnClickListener {
            alertDialog.show()
        }
    }
}

