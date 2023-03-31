package com.example.cars10zes

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
class TimeTracking : Serializable {
    private var startDatetime: LocalDateTime = LocalDateTime.now()
    private var endDatetime: LocalDateTime = LocalDateTime.now()
    private var startPauseDatetime: LocalDateTime = LocalDateTime.now()
    private var endPauseDatetime: LocalDateTime = LocalDateTime.now()

    private var diff = Duration.between(startDatetime, endDatetime)
    private var diffPause = Duration.between(startPauseDatetime, startPauseDatetime)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS") // "yyyy-MM-dd HH:mm:ss.SSS" // change later to HH:mm

    fun startSession(): String {
        startDatetime = LocalDateTime.now()
        return startDatetime.format(formatter)
    }

    fun endSession(): String {
        endDatetime = LocalDateTime.now()
        diff = Duration.between(startDatetime, endDatetime)
        return endDatetime.format(formatter)
    }

    fun getSessionDuration(): String {
        return formatDuration(diff.seconds)
    }

    fun getSessionWorkingTime(): String {
        return formatDuration(diff.seconds-diffPause.seconds)
    }

    fun startPause(): String {
        startPauseDatetime = LocalDateTime.now()
        return startPauseDatetime.format(formatter)
    }

    fun endPause(): String {
        endPauseDatetime = LocalDateTime.now()
        diffPause = Duration.between(startPauseDatetime, endPauseDatetime)
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
}