package com.example.cars10zes

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
class TimeTracking : Serializable {
    //TODO use datastore to save name project or settings
    private var startDatetime: LocalDateTime = LocalDateTime.now()
    private var endDatetime: LocalDateTime = LocalDateTime.now()
    private var startPauseDatetime: LocalDateTime = LocalDateTime.now()
    private var endPauseDatetime: LocalDateTime = LocalDateTime.now()

    private var diff = Duration.between(startDatetime, endDatetime)
    private var diffPause = Duration.between(startPauseDatetime, startPauseDatetime)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS") // "yyyy-MM-dd HH:mm:ss.SSS" // change later to HH:mm
    var status = 0

    fun startSession(name: String, project: String) {
        status = 1
        startDatetime = LocalDateTime.now()
    }

    fun getSessionStartTime(): String {
        return startDatetime.format(formatter)
    }

    fun endSession() {
        status = 4
        endDatetime = LocalDateTime.now()
        diff = Duration.between(startDatetime, endDatetime)
        //TODO insert in db
    }

    fun getSessionEndTime(): String {
        return endDatetime.format(formatter)
    }

    fun getSessionDuration(): String {
        return formatDuration(diff.seconds)
    }

    fun getSessionWorkingTime(): String {
        return formatDuration(diff.seconds-diffPause.seconds)
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
        //TODO replace with select from db
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