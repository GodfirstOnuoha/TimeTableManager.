package com.peacecodes.timetablemanager.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.peacecodes.timetablemanager.models.Authentication

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // create table sql query
    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_FULL_NAME + " TEXT," + COLUMN_REG_NO + " TEXT," + COLUMN_PASSWORD + " TEXT" + ")")

    // drop table sql query
    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Drop User Table if exist
        db?.execSQL(DROP_USER_TABLE)
        // Create tables again
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getAllUser(): List<Authentication> {
        // array of columns to fetch
        val columns = arrayOf(COLUMN_ID, COLUMN_FULL_NAME, COLUMN_USER_NAME, COLUMN_REG_NO, COLUMN_PASSWORD)
        // sorting orders
        val sortOrder = "$COLUMN_USER_NAME ASC"
        val userList = ArrayList<Authentication>()
        val db = this.readableDatabase
        // query the user table
        val cursor = db.query(TABLE_USER, //Table to query
            columns,            //columns to return
            null,     //columns for the WHERE clause
            null,  //The values for the WHERE clause
            null,      //group the rows
            null,       //filter by row groups
            sortOrder)         //The sort order
        if (cursor.moveToFirst()) {
            do {
                val user = Authentication(Id = cursor.getString(cursor.getColumnIndex(COLUMN_ID)).toInt(),
                    username = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    full_name = cursor.getString(cursor.getColumnIndex(COLUMN_FULL_NAME)),
                    reg_no = cursor.getString(cursor.getColumnIndex(COLUMN_REG_NO)),
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)))
                userList.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList
    }

    fun addUser(user: Authentication) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.username)
        values.put(COLUMN_FULL_NAME, user.full_name)
        values.put(COLUMN_PASSWORD, user.password)
        values.put(COLUMN_REG_NO, user.reg_no)
        // Inserting Row
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    fun updateUser(user: Authentication) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.username)
        values.put(COLUMN_FULL_NAME, user.full_name)
        values.put(COLUMN_PASSWORD, user.password)
        values.put(COLUMN_REG_NO, user.reg_no)
        // updating row
        db.update(TABLE_USER, values, "$COLUMN_ID = ?",
            arrayOf(user.Id.toString()))
        db.close()
    }

    fun deleteUser(user: Authentication) {
        val db = this.writableDatabase
        // delete user record by id
        db.delete(TABLE_USER, "$COLUMN_ID = ?",
            arrayOf(user.Id.toString()))
        db.close()
    }

    fun checkUser(reg_no: String): Boolean {
        // array of columns to fetch
        val columns = arrayOf(COLUMN_ID)
        val db = this.readableDatabase
        // selection criteria
        val selection = "$COLUMN_REG_NO = ?"
        // selection argument
        val selectionArgs = arrayOf(reg_no)
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        val cursor = db.query(TABLE_USER, //Table to query
            columns,        //columns to return
            selection,      //columns for the WHERE clause
            selectionArgs,  //The values for the WHERE clause
            null,  //group the rows
            null,   //filter by row groups
            null)  //The sort order
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0) {
            return true
        }
        return false
    }

    fun checkUser(reg_no: String, password: String): Boolean {
        // array of columns to fetch
        val columns = arrayOf(COLUMN_ID)
        val db = this.readableDatabase
        // selection criteria
        val selection = "$COLUMN_REG_NO = ? AND $COLUMN_PASSWORD = ?"
        // selection arguments
        val selectionArgs = arrayOf(reg_no, password)
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        val cursor = db.query(TABLE_USER, //Table to query
            columns, //columns to return
            selection, //columns for the WHERE clause
            selectionArgs, //The values for the WHERE clause
            null,  //group the rows
            null, //filter by row groups
            null) //The sort order
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0)
            return true
        return false
    }

    companion object {
        // Database Version
        private val DATABASE_VERSION = 1
        // Database Name
        private val DATABASE_NAME = "UserManager.db"
        // User table name
        private val TABLE_USER = "user"
        // User Table Columns names
        private val COLUMN_ID = "user_id"
        private val COLUMN_FULL_NAME = "full_name"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_REG_NO = "reg_no"
        private val COLUMN_PASSWORD = "password"
    }
}
