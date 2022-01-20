package com.peacecodes.timetablemanager.models

import java.util.*


data class Data(
    var id: Int = getAutoId(),
    var course_title: String,
    var set_time: String,
    var course_code: String
){
    companion object{
        fun getAutoId(): Int {
            val random = Random()
            return random.nextInt(100)
        }
    }
}
