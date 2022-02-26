package com.peacecodes.timetablemanager.models

data class TimeTable(
    var id: Int = 0,
    var day: String = "",
    var code: String = "",
    var title: String = "",
    var start: String = "",
    var end: String = ""
)
