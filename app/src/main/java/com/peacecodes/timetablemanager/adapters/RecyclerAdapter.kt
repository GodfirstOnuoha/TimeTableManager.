package com.peacecodes.timetablemanager.adapters

import android.app.TimePickerDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.databinding.SingleListItemBinding
import com.peacecodes.timetablemanager.db.TimeTableDbHelper
import com.peacecodes.timetablemanager.models.TimeTable
import com.peacecodes.timetablemanager.util.Util

class RecyclerAdapter(private var timeTableList: ArrayList<TimeTable>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            SingleListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val timeTable = timeTableList[position]
        holder.bindItem(timeTable, position)
    }

    override fun getItemCount() = timeTableList.size

    inner class MyViewHolder(private val binding: SingleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(timeTable: TimeTable, position: Int) {
            binding.courseTitle.text = timeTable.title
            binding.startTime.text = timeTable.start
            binding.endTime.text = timeTable.end
            binding.code.text = timeTable.code

            binding.edit.setOnClickListener {
                val builder = AlertDialog.Builder(binding.root.context)
                val view =
                    LayoutInflater.from(binding.root.context).inflate(R.layout.update_dialog, null)
                builder.setView(view)

                val selectDay = view.findViewById<TextInputLayout>(R.id.select_day_layout)

                val updateBtn = view.findViewById<Button>(R.id.update_btn)
                val code = view.findViewById<TextInputEditText>(R.id.course_code)
                val title = view.findViewById<TextInputEditText>(R.id.course_title)
                val startTime = view.findViewById<TextView>(R.id.start_time_text)
                val endTime = view.findViewById<TextView>(R.id.end_time_text)

                var startHour = 8
                var startMinute = 30
                var currentStartTime = timeTable.start
                startTime.text = currentStartTime

                startTime.setOnClickListener {
                    val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        startHour = hourOfDay
                        startMinute = minute
                        currentStartTime =
                            "${Util.getMainHour(hourOfDay)}:${Util.getMainMinutes(minute)} ${
                                Util.getAmOrPm(
                                hourOfDay
                            )}"
                        startTime.text = currentStartTime
                    }
                    TimePickerDialog(binding.root.context, timeListener, startHour, startMinute, false).show()
                }

                var endHour = 10
                var endMinute = 30
                var currentEndTime = timeTable.end
                endTime.text = currentEndTime

                endTime.setOnClickListener {
                    val timeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        endHour = hourOfDay
                        endMinute = minute
                        currentEndTime =
                            "${Util.getMainHour(hourOfDay)}:${Util.getMainMinutes(minute)} ${
                                Util.getAmOrPm(
                                hourOfDay
                            )}"
                        endTime.text = currentEndTime
                    }
                    TimePickerDialog(binding.root.context, timeListener, endHour, endMinute, false).show()
                }

                selectDay.editText?.setText(timeTable.day)
                code.setText(timeTable.code)
                title.setText(timeTable.title)

                val items =
                    listOf(
                        "Monday",
                        "Tuesday",
                        "Wednesday",
                        "Thursday",
                        "Friday",
                        "Saturday",
                        "Sunday"
                    )
                val adapter = ArrayAdapter(binding.root.context, R.layout.list_item, items)
                (selectDay.editText as? AutoCompleteTextView)?.setAdapter(adapter)

                val alertDialog = builder.create()
                updateBtn.setOnClickListener {
                    if (TextUtils.isEmpty(selectDay.editText?.text) ||
                        TextUtils.isEmpty(code.text) ||
                        TextUtils.isEmpty(title.text) ||
                        TextUtils.isEmpty(startTime.text) ||
                        TextUtils.isEmpty(endTime.text)
                    ) {
                        Toast.makeText(
                            binding.root.context,
                            "Please fill out all the fields!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val newTimeTable = TimeTable(
                            day = selectDay.editText?.text.toString(),
                            code = code.text.toString(),
                            title = title.text.toString(),
                            start = startTime.text.toString(),
                            end = endTime.text.toString(),
                            id = timeTable.id
                        )
                        val dbHelper = TimeTableDbHelper(binding.root.context)
                        val result = dbHelper.updateTimeTable(newTimeTable)
                        if (result) {
                            Toast.makeText(
                                binding.root.context,
                                "Schedule updated successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            timeTableList.add(position, newTimeTable)
                            timeTableList.remove(timeTable)
                            notifyItemChanged(position)
                        } else {
                            Toast.makeText(
                                binding.root.context,
                                "Unable to update Schedules!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        alertDialog.dismiss()
                        title.setText("")
                        startTime.text = ""
                        endTime.text = ""
                        code.setText("")
                        selectDay.editText?.setText("")
                    }
                }
                alertDialog.show()
            }
            binding.delete.setOnClickListener {
                MaterialAlertDialogBuilder(binding.root.context)
                    .setTitle("Delete Schedule?")
                    .setMessage("This action cannot be undone. \nContinue?")
                    .setNegativeButton(
                        "Cancel"
                    ) { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("Yes") { dialog, _ ->
                        val dbHelper = TimeTableDbHelper(binding.root.context)
                        val result = dbHelper.deleteTimeTable(timeTable.id)
                        if (result) {
                            Toast.makeText(
                                binding.root.context,
                                "Schedule deleted successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            timeTableList.remove(timeTable)
                            notifyItemRemoved(position)
                        } else {
                            Toast.makeText(
                                binding.root.context,
                                "Unable to delete Schedule!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        dialog.dismiss()
                    }
                    .show()
            }
        }

    }
}

