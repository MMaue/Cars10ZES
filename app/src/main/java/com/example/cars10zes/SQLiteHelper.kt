package com.example.cars10zes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "timeTracking.db"
        private const val TABLE_USER = "user"
        private const val USER_ID = "user_id"
        private const val USER_NAME = "user_name"
        private const val TABLE_PROJECT = "project"
        private const val PROJECT_ID = "project_id"
        private const val PROJECT_NAME = "project_name"
        private const val TABLE_SESSION = "session"
        private const val SESSION_ID = "session_id"
        private const val SESSION_START = "session_start"
        private const val SESSION_END = "session_end"
        private const val TABLE_PAUSE = "pause"
        private const val PAUSE_ID = "pause_id"
        private const val PAUSE_START = "pause_start"
        private const val PAUSE_END = "pause_end"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE " + TABLE_USER + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_NAME + " TEXT UNIQUE NOT NULL); "
        db?.execSQL(createUserTable)
        val createProjectTable = "CREATE TABLE " + TABLE_PROJECT + " (" +
                PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PROJECT_NAME + " TEXT UNIQUE NOT NULL); "
        db?.execSQL(createProjectTable)
        val createSessionTable = "CREATE TABLE " + TABLE_SESSION + " (" +
                SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SESSION_START + " TEXT NOT NULL, " +
                SESSION_END + " TEXT NOT NULL, " +
                USER_ID + " INTEGER NOT NULL, " +
                PROJECT_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + USER_ID + ") REFERENCES " + TABLE_USER + "(" + USER_ID + "), " +
                "FOREIGN KEY(" + PROJECT_ID + ") REFERENCES " + TABLE_PROJECT + "(" + PROJECT_ID + ")" + "); "
        db?.execSQL(createSessionTable)
        val createPauseTable = "CREATE TABLE " + TABLE_PAUSE + " (" +
                PAUSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PAUSE_START + " TEXT NOT NULL, " +
                PAUSE_END + " TEXT NOT NULL, " +
                SESSION_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + SESSION_ID + ") REFERENCES " + TABLE_SESSION + "(" + SESSION_ID + ")" + "); "
        db?.execSQL(createPauseTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_USER)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAUSE)
        onCreate(db)
    }

    private fun checkSuccess(res: Long) {
        if (res == (-1).toLong()) {
            println("error")
        } else {
            println("dbinsert success")
        }
    }

    private fun getLastID(tableName: String): Int {
        val selectQuery = "SELECT seq FROM sqlite_sequence " +
                "WHERE name=\"" + tableName + "\""
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val lastID = cursor.getInt(0)
        cursor.close()
        db.close()
        return lastID
    }

    fun insertTimeTracking(user: String,
                           project: String,
                           start: String,
                           end: String,
                           pauseList: MutableList<SessionPause>) {
        val userID = insertUser(user)
        val projectID = insertProject(project)
        val sessionID = insertSession(start, end, userID, projectID)
        insertPauses(sessionID, pauseList)
    }

    private fun insertUser(userName: String): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USER_NAME, userName)

        try {
            val res = db.insert(TABLE_USER, null, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        db.close()
        return getUserID(userName)
    }

    private fun getUserID(userName: String): Int {
        val selectQuery = "SELECT " + USER_ID + " FROM " + TABLE_USER + " " +
                "WHERE " + USER_NAME + "=\"" + userName + "\""
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val userID = cursor.getInt(0)
        cursor.close()
        db.close()
        return userID
    }

    private fun insertProject(projectName: String): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(PROJECT_NAME, projectName)

        try {
            val res = db.insert(TABLE_PROJECT, null, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        db.close()
        return getProjectID(projectName)
    }

    private fun getProjectID(projectName: String): Int {
        val selectQuery = "SELECT " + PROJECT_ID + " FROM " + TABLE_PROJECT + " " +
                "WHERE " + PROJECT_NAME + "=\"" + projectName + "\""
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val projectID = cursor.getInt(0)
        cursor.close()
        db.close()
        return projectID
    }

    private fun insertSession(start: String,
                              end: String,
                              userID: Int,
                              projectID: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(SESSION_START, start)
        contentValues.put(SESSION_END, end)
        contentValues.put(USER_ID, userID)
        contentValues.put(PROJECT_ID, projectID)
        val res = db.insert(TABLE_SESSION, null, contentValues)
        db.close()
        checkSuccess(res)
        return getLastID(TABLE_SESSION)
    }

    private fun getSessionID(): Int {
        // select seq from sqlite_sequence where name="table_name"
        val selectQuery = "SELECT seq FROM sqlite_sequence" +
                "WHERE name=" + TABLE_SESSION
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val sessionID = cursor.getInt(0)
        cursor.close()
        db.close()
        return sessionID
    }

    private fun insertPauses(sessionID: Int, pauses: MutableList<SessionPause>) {
        val db = this.writableDatabase

        for (pause in pauses) {
            val contentValues = ContentValues()
            contentValues.put(PAUSE_START, pause.start)
            contentValues.put(PAUSE_END, pause.end)
            contentValues.put(SESSION_ID, sessionID)
            val res = db.insert(TABLE_PAUSE, null, contentValues)
            checkSuccess(res)
        }
        db.close()
    }

    fun getsome(): ArrayList<String> {
        val somlist: ArrayList<String> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_USER"
        val db = this.readableDatabase
        val cursor: Cursor?
        //cursor.moveToFirst()

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList(0)
        }
        return ArrayList(0)
    }

    fun getHistoryList(): MutableList<HistoryItem> {
        return mutableListOf(
            HistoryItem("username",
                "projectname",
                "01.01.1970",
                "00:00",
                "00:00",
                "00:00:00"),
            HistoryItem("user",
                "proj",
                "date1",
                "start",
                "end",
                "duration"),
            HistoryItem("user",
                "proj",
                "date2",
                "start",
                "end",
                "duration")
        )
    }
}