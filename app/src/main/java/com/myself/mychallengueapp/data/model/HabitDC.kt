package com.myself.mychallengueapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity(tableName = "habit")
data class HabitDC(
    @PrimaryKey(autoGenerate = true) val habitId : Int=0,
    val challengeId : Int = 0,
    val description : String = "",
    val tipo : Int = 0, //0 - TimeHabit , 1 - habit
    val duration : Long? = null,
    val timeSpecific: LocalTime? = null
)
