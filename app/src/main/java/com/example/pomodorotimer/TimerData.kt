package com.example.pomodorotimer

data class TimerData (
    val id: Int,
    var currentMs: Long,
    var isStarted: Boolean
)