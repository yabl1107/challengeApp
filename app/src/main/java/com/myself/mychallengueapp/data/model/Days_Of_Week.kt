package com.myself.mychallengueapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days_of_week")
data class Days_Of_Week(
    @PrimaryKey val dayId: Int=0,
    val day: String
)
