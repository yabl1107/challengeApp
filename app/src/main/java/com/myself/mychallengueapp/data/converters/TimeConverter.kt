package com.myself.mychallengueapp.data.converters

import androidx.room.TypeConverter
import java.time.LocalTime

class TimeConverter {
    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? {
        return time?.toString() // Convierte a String
    }

    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? {
        return timeString?.let { LocalTime.parse(it) } // Convierte a LocalTime
    }
}