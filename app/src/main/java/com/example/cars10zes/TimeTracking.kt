package com.example.cars10zes

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
class TimeTracking(context: Context): Serializable {
    //TODO use datastore to save name project or settings
    private var startDatetime: LocalDateTime = LocalDateTime.now()
    private lateinit var endDatetime: LocalDateTime
    private lateinit var startPauseDatetime: LocalDateTime
    private lateinit var endPauseDatetime: LocalDateTime

    private lateinit var diff: Duration
    private lateinit var diffPause: Duration
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")
    private val formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    private var gesPauseSeconds: Long = 0
    var status = 0

    private val sqliteHelper: SQLiteHelper = SQLiteHelper(context)
    private lateinit var user: String
    private lateinit var project: String
    private var pauseList = mutableListOf<SessionPause>()

    fun startSession(user: String, project: String) {
        status = 1
        gesPauseSeconds = 0
        startDatetime = LocalDateTime.now()
        this.user = user
        this.project = project
    }

    fun getSessionStartTime(): String {
        return startDatetime.format(formatter)
    }

    fun endSession() {
        status = 4
        endDatetime = LocalDateTime.now()
        diff = Duration.between(startDatetime, endDatetime)
        sqliteHelper.insertTimeTracking(user,
            project,
            startDatetime.format(formatterDB),
            endDatetime.format(formatterDB),
            pauseList)
    }

    fun getSessionEndTime(): String {
        return endDatetime.format(formatter)
    }

    fun getSessionDuration(): String {
        return formatDuration(diff.seconds)
    }

    fun getSessionWorkingTime(): String {
        return formatDuration(diff.seconds-gesPauseSeconds)
    }

    fun startPause() {
        status = 2
        startPauseDatetime = LocalDateTime.now()
    }

    fun getPauseStartTime(): String {
        return startPauseDatetime.format(formatter)
    }

    fun endPause() {
        status = 3
        endPauseDatetime = LocalDateTime.now()
        diffPause = Duration.between(startPauseDatetime, endPauseDatetime)
        gesPauseSeconds += diffPause.seconds
        pauseList.add(SessionPause(
            startPauseDatetime.format(formatterDB),
            endPauseDatetime.format(formatterDB)))
    }

    fun getPauseEndTime(): String {
        return endPauseDatetime.format(formatter)
    }

    fun getPauseDuration(): String {
        return formatDuration(diffPause.seconds)
    }

    private fun formatDuration(seconds: Long): String {
        var s = seconds
        val h = s / 3600;
        s -= h * 3600;
        val m = s / 60;
        s -= m * 60;
        return "$h:$m:$s"
    }

    fun getHistoryList(): MutableList<HistoryItem> {
        return sqliteHelper.getHistoryList()
    }
}