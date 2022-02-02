package com.peacecodes.timetablemanager.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peacecodes.timetablemanager.databinding.SingleListItemBinding
import com.peacecodes.timetablemanager.models.TimeTable

class RecyclerAdapter(private var timeTableList: List<TimeTable>) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    private var onClickDeleteItem: ((TimeTable)-> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setUpData(timeTable: List<TimeTable>){
        this.timeTableList = timeTable
        notifyDataSetChanged()
    }

    fun onClickDeleteItem(callback: (TimeTable)-> Unit) {
        this.onClickDeleteItem = callback
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
    }

    override fun getItemCount() = timeTableList.size

    inner class MyViewHolder(private val binding: SingleListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(timeTable: TimeTable) {
           binding.courseTitle.text = timeTable.course_title
           binding.startTime.text = timeTable.start_time
           binding.endTime.text = timeTable.end_time
           binding.code.text = timeTable.course_code
            binding.delete.setOnClickListener {
                onClickDeleteItem?.invoke(timeTable)
            }
        }

    }
}

