package com.peacecodes.timetablemanager

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.peacecodes.timetablemanager.models.TimeTable

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "myTimeTable"
        private const val TABLE_NAME = "timeTable"
        private const val ID = "id"
        private const val DAY = "day"
        private const val COURSE_TITLE = "title"
        private const val START_TIME = "start_time"
        private const val END_TIME = "end_time"
        private const val COURSE_CODE = "code"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                ID + " INTEGER PRIMARY KEY, " +
                DAY + " TEXT, " +
                COURSE_TITLE + " TEXT ," +
                START_TIME + " TEXT ," +
                END_TIME + " TEXT ," +
                COURSE_CODE + " TEXT );"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

//    to insert a timetable to the database
    fun insertTimeTable(timeTable: TimeTable): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DAY, timeTable.day)
        values.put(COURSE_TITLE, timeTable.course_title)
        values.put(START_TIME, timeTable.start_time)
        values.put(END_TIME, timeTable.end_time)
        values.put(COURSE_CODE, timeTable.course_code)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }

//    to get a particular timetable
    @SuppressLint("Range")
    fun getTimeTable(_id: Int): TimeTable {
        val timeTable = TimeTable()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = '$_id'"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                timeTable.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                timeTable.course_title = cursor.getString(cursor.getColumnIndex(COURSE_TITLE))
                timeTable.start_time = cursor.getString(cursor.getColumnIndex(START_TIME))
                timeTable.end_time = cursor.getString(cursor.getColumnIndex(END_TIME))
                timeTable.course_code = cursor.getString(cursor.getColumnIndex(COURSE_CODE))
                timeTable.day = cursor.getString(cursor.getColumnIndex(DAY))
            }
        }
        cursor.close()
        return timeTable
    }

    //    to get a particular timetable
    @SuppressLint("Range")
    fun getTime(_time: String): List<TimeTable> {
        val timeTableList = ArrayList<TimeTable>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $START_TIME = '$_time'"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val timeTable = TimeTable()
                timeTable.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                timeTable.course_title = cursor.getString(cursor.getColumnIndex(COURSE_TITLE))
                timeTable.start_time = cursor.getString(cursor.getColumnIndex(START_TIME))
                timeTable.end_time = cursor.getString(cursor.getColumnIndex(END_TIME))
                timeTable.course_code = cursor.getString(cursor.getColumnIndex(COURSE_CODE))
                timeTable.day = cursor.getString(cursor.getColumnIndex(DAY))
                timeTableList.add(timeTable)
            }
        }
        cursor.close()
        return timeTableList
    }

    //    to read all data stored in the database
    @SuppressLint("Range")
    fun getAllTimeTable(): List<TimeTable> {
        val timeTableList = ArrayList<TimeTable>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val timeTable = TimeTable()
                timeTable.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                timeTable.course_title = cursor.getString(cursor.getColumnIndex(COURSE_TITLE))
                timeTable.start_time = cursor.getString(cursor.getColumnIndex(START_TIME))
                timeTable.end_time = cursor.getString(cursor.getColumnIndex(END_TIME))
                timeTable.course_code = cursor.getString(cursor.getColumnIndex(COURSE_CODE))
                timeTable.day = cursor.getString(cursor.getColumnIndex(DAY))
                timeTableList.add(timeTable)
            }
        }
        cursor.close()
        return timeTableList
    }

    //    to read all timetable for monday data stored in the database
    @SuppressLint("Range")
    fun getDayTimeTable(_day: String): List<TimeTable> {
        val timeTableList = ArrayList<TimeTable>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $DAY = '$_day'"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val timeTable = TimeTable()
                timeTable.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                timeTable.course_title = cursor.getString(cursor.getColumnIndex(COURSE_TITLE))
                timeTable.start_time = cursor.getString(cursor.getColumnIndex(START_TIME))
                timeTable.end_time = cursor.getString(cursor.getColumnIndex(END_TIME))
                timeTable.course_code = cursor.getString(cursor.getColumnIndex(COURSE_CODE))
                timeTable.day = cursor.getString(cursor.getColumnIndex(DAY))
                timeTableList.add(timeTable)
            }
        }
        cursor.close()
        return timeTableList
    }

    //    to update a particular Timetable in the database
    fun updateTimeTable(timeTable: TimeTable): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DAY, timeTable.day)
        values.put(COURSE_TITLE, timeTable.course_title)
        values.put(START_TIME, timeTable.start_time)
        values.put(END_TIME, timeTable.end_time)
        values.put(COURSE_CODE, timeTable.course_code)
        val _success =
            db.update(TABLE_NAME, values, ID + "=?", arrayOf(timeTable.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

//    to delete a particular timeTable in the database
    fun deleteTimeTable(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }
}