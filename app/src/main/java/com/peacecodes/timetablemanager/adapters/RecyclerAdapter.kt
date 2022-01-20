package com.peacecodes.timetablemanager.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.models.Data

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
private var ttbList: ArrayList<Data> = ArrayList()

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
    fun addItems(items: ArrayList<Data>){
        this.ttbList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.activity_editor, parent, false )
        return MyViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ttb = ttbList[position]
        holder.bindViews(ttb)
        holder.clockItem.setImageResource(R.drawable.alarm)
    }

    override fun getItemCount() = ttbList.size

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var timeItem : TextView
        var courseItem : TextView
        var codeItem : TextView
        var clockItem : ImageView

        init {
            timeItem = view.findViewById(R.id.time)
            courseItem = view.findViewById(R.id.course_title)
            codeItem = view.findViewById(R.id.code)
            clockItem = view.findViewById(R.id.alarm)
        }
        fun bindViews(ttb: Data){
            timeItem.text = ttb.set_time.toString()
            courseItem.text = ttb.course_title.toString()
            codeItem.text = ttb.course_code.toString()
        }
    }
}