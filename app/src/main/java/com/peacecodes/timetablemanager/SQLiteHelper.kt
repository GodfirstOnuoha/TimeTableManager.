package com.peacecodes.timetablemanager

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.peacecodes.timetablemanager.models.Data
import java.lang.Exception
import kotlin.collections.ArrayList

class SQLiteHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME, null, DATABASE_VERSION) {

   companion object{

       private const val DATABASE_VERSION = 1
       private const val DATABASE_NAME = "student.db"
       private const val STUDENT_TTB = "student_ttb"
       private const val COLUMN_ID = "id"
       private const val COLUMN_DAY = "day"
       private const val COLUMN_TITLE = "title"
       private const val COLUMN_TIME = "time"
       private const val COLUMN_CODE = "code"


   }

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_TABLE = ("CREATE TABLE " + STUDENT_TTB + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_DAY + " TEXT ," +
                COLUMN_TITLE + " TEXT ," +
                COLUMN_TIME + " TEXT ," +
                COLUMN_CODE + " TEXT " + ")")
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $STUDENT_TTB")
        onCreate(db)
    }

    fun insertTimeTable(ttb: Data): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID, ttb.id)
        contentValues.put(COLUMN_TITLE, ttb.course_title)
        contentValues.put(COLUMN_TIME, ttb.set_time)
        contentValues.put(COLUMN_CODE, ttb.course_code)

        val success = db.insert(STUDENT_TTB, null,contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllSchedules(): ArrayList<Data>{
        val ttbList: ArrayList<Data> = ArrayList()
        val selectQuery = "SELECT * FROM $STUDENT_TTB"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var course_title: String
        var set_time: String
        var course_code: String

        if (cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                course_title = cursor.getString(cursor.getColumnIndex("course_title"))
                set_time = cursor.getString(cursor.getColumnIndex("set_time"))
                course_code = cursor.getString(cursor.getColumnIndex("course_code"))
                val ttb = Data(id = id, course_title = course_title, set_time = set_time, course_code = course_code)
                ttbList.add(ttb)
            }while (cursor.moveToNext())
        }
        return  ttbList
    }
}