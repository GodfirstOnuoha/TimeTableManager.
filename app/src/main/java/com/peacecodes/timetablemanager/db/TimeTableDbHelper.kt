package com.peacecodes.timetablemanager.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.peacecodes.timetablemanager.db.TimeTableDbHelper.Companion.TimeTableEntry.COLUMN_NAME_CODE
import com.peacecodes.timetablemanager.db.TimeTableDbHelper.Companion.TimeTableEntry.COLUMN_NAME_DAY
import com.peacecodes.timetablemanager.db.TimeTableDbHelper.Companion.TimeTableEntry.COLUMN_NAME_END
import com.peacecodes.timetablemanager.db.TimeTableDbHelper.Companion.TimeTableEntry.COLUMN_NAME_START
import com.peacecodes.timetablemanager.db.TimeTableDbHelper.Companion.TimeTableEntry.COLUMN_NAME_TITLE
import com.peacecodes.timetablemanager.db.TimeTableDbHelper.Companion.TimeTableEntry.TABLE_NAME
import com.peacecodes.timetablemanager.models.TimeTable

class TimeTableDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun insertTimeTable(timeTable: TimeTable): Boolean {
        val db = this.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(COLUMN_NAME_DAY, timeTable.day)
            put(COLUMN_NAME_CODE, timeTable.code)
            put(COLUMN_NAME_TITLE, timeTable.title)
            put(COLUMN_NAME_START, timeTable.start)
            put(COLUMN_NAME_END, timeTable.end)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert(TABLE_NAME, null, values)
        return (Integer.parseInt("$newRowId") != -1)
    }

    fun getDayTimeTable(_day: String): ArrayList<TimeTable> {
        val db = this.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_DAY,
            COLUMN_NAME_CODE,
            COLUMN_NAME_TITLE,
            COLUMN_NAME_START,
            COLUMN_NAME_END
        )

        // Filter results WHERE "day" = 'Any day'
        val selection = "$COLUMN_NAME_DAY = ?"
        val selectionArgs = arrayOf(_day)

        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${BaseColumns._ID} ASC"

        val cursor = db.query(
            TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )

        val timeTableList = arrayListOf<TimeTable>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val day = getString(getColumnIndexOrThrow(COLUMN_NAME_DAY))
                val code = getString(getColumnIndexOrThrow(COLUMN_NAME_CODE))
                val title = getString(getColumnIndexOrThrow(COLUMN_NAME_TITLE))
                val start = getString(getColumnIndexOrThrow(COLUMN_NAME_START))
                val end = getString(getColumnIndexOrThrow(COLUMN_NAME_END))
                timeTableList.add(TimeTable(id, day, code, title, start, end))
            }
        }
        cursor.close()
        return timeTableList
    }

    fun updateTimeTable(timeTable: TimeTable): Boolean {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME_DAY, timeTable.day)
            put(COLUMN_NAME_CODE, timeTable.code)
            put(COLUMN_NAME_TITLE, timeTable.title)
            put(COLUMN_NAME_START, timeTable.start)
            put(COLUMN_NAME_END, timeTable.end)
        }

        // Which row to update, based on the title
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf("${timeTable.id}")
        val count = db.update(
            TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
        return count > 0
    }

    fun deleteTimeTable(_id: Int): Boolean {
        val db = this.writableDatabase

        // Define 'where' part of query.
        val selection = "${BaseColumns._ID} LIKE ?"

        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf("$_id")

        // Issue SQL statement.
        val deletedRows = db.delete(TABLE_NAME, selection, selectionArgs)
        return deletedRows > 0
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "TimeTable.db"

        object TimeTableEntry : BaseColumns {
            const val TABLE_NAME = "timetable"
            const val COLUMN_NAME_DAY = "day"
            const val COLUMN_NAME_CODE = "code"
            const val COLUMN_NAME_TITLE = "title"
            const val COLUMN_NAME_START = "start"
            const val COLUMN_NAME_END = "end"
        }

        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "$COLUMN_NAME_DAY TEXT," +
                    "$COLUMN_NAME_CODE TEXT," +
                    "$COLUMN_NAME_TITLE TEXT," +
                    "$COLUMN_NAME_START TEXT," +
                    "$COLUMN_NAME_END TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}