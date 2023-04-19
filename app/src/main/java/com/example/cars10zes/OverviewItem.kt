package com.example.cars10zes

data class OverviewItem(
    val nameProject: String,
    var projectDuration: String,
    var projectPauseDuration: String,
    var projectDurations: MutableList<ProjectDuration>
)