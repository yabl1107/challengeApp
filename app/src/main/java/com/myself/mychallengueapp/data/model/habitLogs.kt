package com.myself.mychallengueapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "habit_logs")
data class habitLogs(
    @PrimaryKey(autoGenerate = true) val logId: Long = 0,
    val habitId: Int,
    val date: LocalDate
)
