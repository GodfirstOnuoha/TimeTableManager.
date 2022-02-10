package com.peacecodes.timetablemanager.models

data class Authentication(
    val Id: Int = -1,
    val full_name: String,
    val username: String,
    val reg_no: String,
    val password: String
)