package com.peacecodes.timetablemanager.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peacecodes.timetablemanager.databinding.SingleListItemBinding
import com.peacecodes.timetablemanager.models.TimeTable

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
private val timeTableList = mutableListOf<TimeTable>()

//    companion object{
//     val times = arrayOf(
//        "8:00AM - 9:00AM",
//        "9:00AM - 10:00AM",
//        "10:00AM - 11:00AM",
//        "11:00AM - 12:00PM",
//        "12:00PM - 1:00PM",
//        "1:00PM - 2:00PM"
//    )
//
//     val course_titles = arrayOf(
//        "Software Engineering",
//        "Data Structures",
//        "Website Design",
//        "Information Technology",
//        "Statistics I",
//        "Numerical Method I"
//    )
//
//     val codes = arrayOf(
//        "CSC 226",
//        "CSC 331",
//        "CSC 401",
//        "CSC 221",
//        "STAT 331",
//        "CSC 232"
//    )
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(timeTable: List<TimeTable>){
        this.timeTableList.addAll(timeTable)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(SingleListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val timeTable = timeTableList[position]
        holder.bindItem(timeTable)
//        holder.clockItem.setImageResource(R.drawable.alarm)
    }

    override fun getItemCount() = timeTableList.size

    inner class MyViewHolder(private val binding: SingleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(timeTable: TimeTable) {
           binding.courseTitle.text = timeTable.course_title
           binding.time.text = timeTable.start_time
           binding.time.text = timeTable.end_time
           binding.code.text = timeTable.course_code
//           binding.alarm.setImageDrawable(R.drawable.alarm)
        }

    }
}

