package com.example.cars10zes

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
class TimeTracking(context: Context): Serializable {
    private var startDatetime: LocalDateTime = LocalDateTime.now()
    private lateinit var endDatetime: LocalDateTime
    private lateinit var startPauseDatetime: LocalDateTime
    private lateinit var endPauseDatetime: LocalDateTime

    private lateinit var diff: Duration
    private lateinit var diffPause: Duration
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")
    private val formatterDB = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    private val formatterCal = DateTimeFormatter.ofPattern("MMMM yyyy")

    private var gesPauseSeconds: Long = 0
    var status = 0

    private val sqliteHelper: SQLiteHelper = SQLiteHelper(context)
    private lateinit var user: String
    private lateinit var project: String

    var selectedDate = LocalDate.now()
    lateinit var selectedProject: String

    fun startSession(user: String, project: String) {
        status = 1
        gesPauseSeconds = 0
        startDatetime = LocalDateTime.now()
        this.user = user
        this.project = project
        sqliteHelper.insertSessionStart(user,
            project,
            startDatetime.format(formatterDB))
    }

    fun getSessionStartTime(): String {
        return startDatetime.format(formatter)
    }

    fun endSession() {
        status = 4
        endDatetime = LocalDateTime.now()
        diff = Duration.between(startDatetime, endDatetime)
        sqliteHelper.insertSessionEnd(endDatetime.format(formatterDB))
        gesPauseSeconds = sqliteHelper.getLastPauseTime()
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
        sqliteHelper.insertPauseStart(startPauseDatetime.format(formatterDB))
    }

    fun getPauseStartTime(): String {
        return try {
            startPauseDatetime.format(formatter)
        } catch (_: UninitializedPropertyAccessException) {
            ""
        }
    }

    fun endPause() {
        status = 3
        endPauseDatetime = LocalDateTime.now()
        diffPause = Duration.between(startPauseDatetime, endPauseDatetime)
        gesPauseSeconds += diffPause.seconds
        sqliteHelper.insertPauseEnd(endPauseDatetime.format(formatterDB))
    }

    fun getPauseEndTime(): String {
        return try {
            endPauseDatetime.format(formatter)
        } catch (_: UninitializedPropertyAccessException) {
            ""
        }
    }

    fun getPauseDuration(): String {
        return try {
            formatDuration(diffPause.seconds)
        } catch (_: UninitializedPropertyAccessException) {
            ""
        }
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
        val historyList =  sqliteHelper.getHistoryList()
        for (historyItem in historyList) {
            val sessionSec = historyItem.sessionDuration.toLong()
            val pauseSec = historyItem.pauseDuration.toLong()
            historyItem.sessionDuration = formatDuration(sessionSec-pauseSec)
            historyItem.pauseDuration = formatDuration(pauseSec)
        }
        return historyList
    }

    fun getOverviewList(): MutableList<OverviewItem> {
        val overviewList =  sqliteHelper.getOverviewList()
        for (overviewItem in overviewList) {
            val projectSec = overviewItem.projectDuration.toLong()
            val projectPauseSec = overviewItem.projectPauseDuration.toLong()
            overviewItem.projectDuration = formatDuration(projectSec-projectPauseSec)
            overviewItem.projectPauseDuration = formatDuration(projectPauseSec)
        }
        return overviewList
    }

    fun getDaysOfMonth(): ArrayList<CalendarItem> {
        val calendarItems = ArrayList<CalendarItem>()
        val yearMonth = YearMonth.from(selectedDate)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value-1

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                calendarItems.add(CalendarItem(
                    "",
                    "",
                    false
                ))
            } else {
                val dayOfMonth = i-dayOfWeek
                val dateString = selectedDate.withDayOfMonth(dayOfMonth).toString()
                calendarItems.add(
                    CalendarItem(
                        dayOfMonth.toString(),
                        dateString,
                        sqliteHelper.sessionFoundOn(dateString, selectedProject)
                ))
            }
        }
        return calendarItems
    }

    fun getCalendarDate(): String {
        return selectedDate.format(formatterCal)
    }

    fun getProjectsList(): ArrayList<String> {
        return sqliteHelper.getProjectsList()
    }

    fun getLastUser(): String {
        user = sqliteHelper.getLastUser()
        return user
    }

    fun getLastProject(): String {
        project = sqliteHelper.getLastProject()
        return project
    }

    fun restoreStatus() {
        var sessionNull = false
        var pauseNull = false
        try {
            sessionNull = sqliteHelper.sessionEndNull()
        } catch (_: Exception) {

        }
        try {
            pauseNull = sqliteHelper.pauseEndNull()
        } catch (_: Exception) {

        }
        if (!sessionNull && !pauseNull) {
            status = 0
        } else if (sessionNull && !pauseNull) {
            status = 1
            startDatetime = LocalDateTime.parse(sqliteHelper.getLastSessionStart(), formatterDB)
        } else if (sessionNull && pauseNull) {
            status = 2
            startDatetime = LocalDateTime.parse(sqliteHelper.getLastSessionStart(), formatterDB)
            startPauseDatetime = LocalDateTime.parse(sqliteHelper.getLastPauseStart(), formatterDB)
        }
    }
}