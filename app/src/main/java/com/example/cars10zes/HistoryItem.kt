package com.example.cars10zes

data class HistoryItem(
    val nameUser: String,
    val nameProject: String,
    val date: String,
    val sessionStart: String,
    val sessionEnd: String,
    var sessionDuration: String,
    var pauseDuration: String
)