package com.myself.mychallengueapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myself.mychallengueapp.data.converters.DateConverter
import com.myself.mychallengueapp.data.converters.TimeConverter
import com.myself.mychallengueapp.data.dao.DaysOfWeekDAO
import com.myself.mychallengueapp.data.dao.challengeDAO
import com.myself.mychallengueapp.data.dao.habitDAO
import com.myself.mychallengueapp.data.dao.habitDaysDAO
import com.myself.mychallengueapp.data.dao.habitLogsDAO
import com.myself.mychallengueapp.data.model.ChallengeDC
import com.myself.mychallengueapp.data.model.Days_Of_Week
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.model.Habit_Days
import com.myself.mychallengueapp.data.model.habitLogs

@Database(
    entities = [
        ChallengeDC::class,
        Days_Of_Week::class,
        HabitDC::class,
        Habit_Days::class,
        habitLogs::class],
    version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, TimeConverter::class)
abstract class appDatabase : RoomDatabase(){
    abstract fun challengeDAO(): challengeDAO
    abstract fun daysOfWeekDAO(): DaysOfWeekDAO
    abstract fun habitDAO(): habitDAO
    abstract fun habitDaysDAO(): habitDaysDAO
    abstract fun habitLogsDAO(): habitLogsDAO
}