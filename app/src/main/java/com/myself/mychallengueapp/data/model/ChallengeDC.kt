package com.myself.mychallengueapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "challenge")
data class ChallengeDC(
    @PrimaryKey(autoGenerate = true) val challengeId : Int = 0,
    val description : String,
    val reason : String,
    val endDate: LocalDate? = null
)
