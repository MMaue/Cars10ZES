package com.example.cars10zes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
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
                SESSION_END + " TEXT, " +
                USER_ID + " INTEGER NOT NULL, " +
                PROJECT_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + USER_ID + ") REFERENCES " + TABLE_USER + "(" + USER_ID + "), " +
                "FOREIGN KEY(" + PROJECT_ID + ") REFERENCES " + TABLE_PROJECT + "(" + PROJECT_ID + ")" + "); "
        db?.execSQL(createSessionTable)
        val createPauseTable = "CREATE TABLE " + TABLE_PAUSE + " (" +
                PAUSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PAUSE_START + " TEXT NOT NULL, " +
                PAUSE_END + " TEXT, " +
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

    fun insertSessionStart(user: String,
                           project: String,
                           start: String) {
        val userID = insertUser(user)
        val projectID = insertProject(project)
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(SESSION_START, start)
        // SESSION_END NULL
        contentValues.put(USER_ID, userID)
        contentValues.put(PROJECT_ID, projectID)
        db.insert(TABLE_SESSION, null, contentValues)
        db.close()
    }

    fun insertSessionEnd(end: String) {
        val sessionID = getLastSessionID()
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(SESSION_END, end)
        db.update(TABLE_SESSION, contentValues, SESSION_ID + " = " + sessionID, null)
        db.close()
    }

    private fun insertUser(userName: String): Int {
        //TODO first check if id exists then return or insert
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USER_NAME, userName)

        try {
            db.insertOrThrow(TABLE_USER, null, contentValues)
        } catch (_: SQLiteConstraintException) {

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
        //TODO first check if id exists then return or insert
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(PROJECT_NAME, projectName)

        try {
            db.insertOrThrow(TABLE_PROJECT, null, contentValues)
        } catch (_: SQLiteConstraintException) {

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

    private fun getLastSessionID(): Int {
        val selectQuery = "SELECT seq FROM sqlite_sequence " +
                "WHERE name=\"" + TABLE_SESSION + "\""
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val lastID = cursor.getInt(0)
        cursor.close()
        db.close()
        return lastID
    }

    fun getLastSessionStart(): String {
        val sessionID = getLastSessionID()
        val selectQuery = "SELECT " + SESSION_START + " " +
                "FROM " + TABLE_SESSION + " " +
                "WHERE " + SESSION_ID + " = " + sessionID
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val sessionStart = cursor.getString(0)
        cursor.close()
        db.close()
        return sessionStart
    }

    private fun getLastPauseID(): Int {
        val selectQuery = "SELECT seq FROM sqlite_sequence " +
                "WHERE name=\"" + TABLE_PAUSE + "\""
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val lastID = cursor.getInt(0)
        cursor.close()
        db.close()
        return lastID
    }

    fun getLastPauseStart(): String {
        val pauseID = getLastPauseID()
        val selectQuery = "SELECT " + PAUSE_START + " " +
                "FROM " + TABLE_PAUSE + " " +
                "WHERE " + PAUSE_ID + " = " + pauseID
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val pauseStart = cursor.getString(0)
        cursor.close()
        db.close()
        return pauseStart
    }

    fun insertPauseStart(start: String) {
        val sessionID = getLastSessionID()
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(PAUSE_START, start)
        // PAUSE_END NULL
        contentValues.put(SESSION_ID, sessionID)
        db.insert(TABLE_PAUSE, null, contentValues)
        db.close()
    }

    fun insertPauseEnd(end: String) {
        val pauseID = getLastPauseID()
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(PAUSE_END, end)
        db.update(TABLE_PAUSE, contentValues, PAUSE_ID + " = " + pauseID, null)
        db.close()
    }

    fun getHistoryList(): MutableList<HistoryItem> {
        val historyList = mutableListOf<HistoryItem>()
        val selectQuery = "SELECT " + USER_NAME + ", " + PROJECT_NAME + ", " +
                "date(" + SESSION_START + ") AS startdate, " +
                "substr(time(" + SESSION_START + "), 0, 6) AS starttime, " +
                "CASE WHEN " + SESSION_END + " IS NULL " +
                "THEN \"\" " +
                "ELSE substr(time(" + SESSION_END + "), 0, 6) " +
                "END AS endtime, " +
                "CASE WHEN " + SESSION_END + " IS NULL " +
                "THEN CAST((julianday(\"now\", \"localtime\") - julianday(" + SESSION_START + ")) * 24 * 60 * 60 AS INTEGER) " +
                "ELSE CAST((julianday(" + SESSION_END + ") - julianday(" + SESSION_START + ")) * 24 * 60 * 60 AS INTEGER) " +
                "END AS duration_ges, " +
                "CASE WHEN pausetimediff IS NOT NULL " +
                "THEN CAST(pausetimediff * 24 * 60 * 60 AS INTEGER) " +
                "ELSE 0 " +
                "END AS duration_pause " +
                "FROM " + TABLE_SESSION + " " +
                "INNER JOIN " + TABLE_USER + " " +
                "ON " + TABLE_USER + "." + USER_ID + " = " + TABLE_SESSION + "." + USER_ID + " " +
                "INNER JOIN " + TABLE_PROJECT + " " +
                "ON " + TABLE_PROJECT + "." + PROJECT_ID + " = " + TABLE_SESSION + "." + PROJECT_ID + " " +
                "LEFT JOIN (SELECT " + SESSION_ID + ", " +
                "sum(julianday(" + PAUSE_END + ") - julianday(" + PAUSE_START + ")) AS pausetimediff " +
                "FROM " + TABLE_PAUSE + " " +
                "GROUP BY " + SESSION_ID + ") AS res " +
                "ON res." + SESSION_ID + " = " + TABLE_SESSION + "." + SESSION_ID + " " +
                "ORDER BY " + TABLE_SESSION + "." + SESSION_START + " DESC"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return historyList
        }

        var userName: String
        var projectName: String
        var sessionDate: String
        var sessionStart: String
        var sessionEnd: String
        var sessionDuration: String
        var pauseDuration: String

        if (cursor.moveToFirst()) {
            do {
                userName = cursor.getString(0)
                projectName = cursor.getString(1)
                sessionDate = cursor.getString(2)
                sessionStart = cursor.getString(3)
                sessionEnd = cursor.getString(4)
                sessionDuration = cursor.getString(5)
                pauseDuration = cursor.getString(6)

                val historyItem = HistoryItem(
                    userName,
                    projectName,
                    sessionDate,
                    sessionStart,
                    sessionEnd,
                    sessionDuration,
                    pauseDuration)
                historyList.add(historyItem)
            } while (cursor.moveToNext())
        }

        return historyList
    }

    fun getLastUser(): String {
        var userName = ""
        val selectQuery = "SELECT " + USER_NAME + " FROM " + TABLE_SESSION + " " +
                "INNER JOIN " + TABLE_USER + " " +
                "ON " + TABLE_USER + "." + USER_ID + " = " + TABLE_SESSION + "." + USER_ID + " " +
                "ORDER BY " + TABLE_SESSION + "." + SESSION_START + " DESC LIMIT 1"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
            cursor.moveToFirst()
            userName = cursor.getString(0)
            cursor.close()
        } catch (_: CursorIndexOutOfBoundsException) {
            userName = ""
        }
        db.close()
        return userName
    }

    fun getLastProject(): String {
        var projectName = ""
        val selectQuery = "SELECT " + PROJECT_NAME + " FROM " + TABLE_SESSION + " " +
                "INNER JOIN " + TABLE_PROJECT + " " +
                "ON " + TABLE_PROJECT + "." + PROJECT_ID + " = " + TABLE_SESSION + "." + PROJECT_ID + " " +
                "ORDER BY " + TABLE_SESSION + "." + SESSION_START + " DESC LIMIT 1"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
            cursor.moveToFirst()
            projectName = cursor.getString(0)
            cursor.close()
        } catch (_: CursorIndexOutOfBoundsException) {
            projectName = ""
        }
        db.close()
        return projectName
    }

    fun sessionEndNull(): Boolean {
        val sessionID = getLastSessionID()
        val selectQuery = "SELECT " + SESSION_END + " IS NULL " +
                "FROM " + TABLE_SESSION + " " +
                "WHERE " + SESSION_ID + " = " + sessionID
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val res = cursor.getInt(0)
        cursor.close()
        db.close()
        return res==1
    }

    fun pauseEndNull(): Boolean {
        val pauseID = getLastPauseID()
        val selectQuery = "SELECT " + PAUSE_END + " IS NULL " +
                "FROM " + TABLE_PAUSE + " " +
                "WHERE " + PAUSE_ID + " = " + pauseID
        val db = this.readableDatabase
        val cursor: Cursor?
        cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val res = cursor.getInt(0)
        cursor.close()
        db.close()
        return res==1
    }

    fun getLastPauseTime(): Long {
        var res: Long
        val sessionID = getLastSessionID()
        val selectQuery = "SELECT CAST(sum(julianday(" + PAUSE_END + ") - julianday(" + PAUSE_START + ")) * 24 * 60 * 60 AS INTEGER) " +
                "FROM " + TABLE_PAUSE + " " +
                "WHERE " + SESSION_ID + " = " + sessionID + " " +
                "GROUP BY " + SESSION_ID
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
            cursor.moveToFirst()
            res = cursor.getLong(0)
            cursor.close()
        } catch (_: CursorIndexOutOfBoundsException) {
            res = 0
        }
        db.close()
        return res
    }

    fun getOverviewList(): MutableList<OverviewItem> {
        val overviewList = mutableListOf<OverviewItem>()
        val selectQuery = "SELECT " + PROJECT_NAME + ", sum(duration_ges), sum(duration_pause) " +
                "FROM (SELECT " + PROJECT_NAME + ", " +
                "CASE WHEN " + SESSION_END + " IS NULL " +
                "THEN 0 " +
                "ELSE CAST((julianday(" + SESSION_END + ") - julianday(" + SESSION_START + ")) * 24 * 60 * 60 AS INTEGER) " +
                "END AS duration_ges, " +
                "CASE WHEN pausetimediff IS NOT NULL " +
                "THEN CAST(pausetimediff * 24 * 60 * 60 AS INTEGER) " +
                "ELSE 0 " +
                "END AS duration_pause " +
                "FROM " + TABLE_SESSION + " " +
                "INNER JOIN " + TABLE_PROJECT + " " +
                "ON " + TABLE_PROJECT + "." + PROJECT_ID + " = " + TABLE_SESSION+ "." + PROJECT_ID + " " +
                "LEFT JOIN (SELECT " + SESSION_ID + ", " +
                "sum(julianday(" + PAUSE_END + ") - julianday(" + PAUSE_START + ")) AS pausetimediff " +
                "FROM " + TABLE_PAUSE + " " +
                "GROUP BY " + SESSION_ID + ") AS res " +
                "ON res." + SESSION_ID + " = " + TABLE_SESSION + "." + SESSION_ID + ") AS res " +
                "GROUP BY " + PROJECT_NAME
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return overviewList
        }

        var projectName: String
        var projectDuration: String
        var projectPauseDuration: String

        if (cursor.moveToFirst()) {
            do {
                projectName = cursor.getString(0)
                projectDuration = cursor.getString(1)
                projectPauseDuration = cursor.getString(2)

                val overviewItem = OverviewItem(
                    projectName,
                    projectDuration,
                    projectPauseDuration)
                overviewList.add(overviewItem)
            } while (cursor.moveToNext())
        }

        return overviewList

    }
}