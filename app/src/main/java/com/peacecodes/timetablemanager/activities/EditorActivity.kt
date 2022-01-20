package com.peacecodes.timetablemanager.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.peacecodes.timetablemanager.SQLiteHelper
import com.peacecodes.timetablemanager.databinding.ActivityEditorBinding
import com.peacecodes.timetablemanager.models.Data

class EditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditorBinding
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var course_title: EditText
    private lateinit var set_time: EditText
    private lateinit var course_code: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        sqLiteHelper = SQLiteHelper(this)
        binding.saveBtn.setOnClickListener { addSchedule() }

    }

    private fun addSchedule(){
        val course_title = course_title.text.toString()
        val set_time = set_time.text.toString()
        val course_code = course_code.text.toString()

        if (course_title.isEmpty() || set_time.isEmpty() || course_code.isEmpty()){
            Toast.makeText(this,"Enter required fields", Toast.LENGTH_SHORT).show()
        } else {
            val ttb = Data(course_title = course_title, set_time = set_time, course_code = course_code)
            val status = sqLiteHelper.insertTimeTable(ttb)

            if (status > -1) {
                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
                clearEditText()
            } else {
                Toast.makeText(this, "Not added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearEditText() {
        course_title.setText("")
        set_time.setText("")
        course_code.setText("")
        course_title.requestFocus()
    }


    private fun initView() {
        binding.courseTitle
        binding.setTime
        binding.courseCode
        binding.saveBtn
    }

}
