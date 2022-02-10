package com.peacecodes.timetablemanager.activities

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.adapters.ViewPagerAdapter
import com.peacecodes.timetablemanager.databinding.ActivityHomeBinding
import com.peacecodes.timetablemanager.db.SQLiteHelper
import com.peacecodes.timetablemanager.models.TimeTable

class Home : AppCompatActivity() {
    private lateinit var sqliteHelper: SQLiteHelper
private lateinit var binding: ActivityHomeBinding
    var tabTitle =
        arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sqliteHelper = SQLiteHelper(this)

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

        val SelectDay = view.findViewById<TextInputLayout>(R.id.select_day_layout)
        val items = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val adapter = ArrayAdapter(this@Home, R.layout.list_item, items)
        (SelectDay.editText as? AutoCompleteTextView)?.setAdapter(adapter)

//        val spinner = view.findViewById<Spinner>(R.id.select_day)
//        val adapter = ArrayAdapter(this@Home,
//            android.R.layout.simple_spinner_item, resources.getStringArray(R.array.days))
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.onItemSelectedListener
//        spinner.adapter


        val saveBtn = view.findViewById<Button>(R.id.save_btn)
        val title = view.findViewById<TextInputEditText>(R.id.course_title)
        val start_time = view.findViewById<TextInputEditText>(R.id.start_time)
        val end_time = view.findViewById<TextInputEditText>(R.id.end_time)
        val code = view.findViewById<TextInputEditText>(R.id.course_code)

        val alertDialog = builder.create()
        saveBtn.setOnClickListener {
           val result = sqliteHelper.insertTimeTable(
               TimeTable(
               day = SelectDay.editText?.text.toString(), course_title = title.text.toString(),
               start_time = start_time.text.toString(), end_time = end_time.text.toString(),
               course_code = code.text.toString())
           )
            if (result == true){
                Toast.makeText(this, "Record Saved", Toast.LENGTH_SHORT).show()
                Log.e("ppppp", "${sqliteHelper.getAllTimeTable().size} ")
                sqliteHelper.getAllTimeTable()
            } else {
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
            }
           alertDialog.dismiss()
            title.setText("")
            start_time.setText("")
            end_time.setText("")
            code.setText("")
            SelectDay.editText?.setText("")
//            Toast.makeText(this, "${SelectDay.editText?.text}", Toast.LENGTH_SHORT).show()
        }

        binding.floatingActionButton.setOnClickListener {
            alertDialog.show()
        }
    }
    fun clearData(){
//        title.text
//        start_time.setText("")
//        end_time.setText("")
//        code.setText("")
//        spinner.clearFocus()
    }
}

