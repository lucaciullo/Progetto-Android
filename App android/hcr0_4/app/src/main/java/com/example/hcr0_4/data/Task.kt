package com.example.hcr0_4.data

import java.time.LocalDate

data class Task(
    val date: LocalDate,
    val time: String,
    val title: String,
    val details: String,
    val type: TaskType
)

enum class TaskType {
    MEDICATION, MEAL, WORKOUT
}
