package com.peacecodes.timetablemanager.models

import java.util.*


data class TimeTable(
    var id: Int = 0,
    var day: String = "",
    var course_title: String = "",
    var start_time: String = "",
    var end_time: String = "",
    var course_code: String = ""
)
