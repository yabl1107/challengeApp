package com.myself.mychallengueapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.myself.mychallengueapp.data.model.Days_Of_Week
import com.myself.mychallengueapp.data.relations.daysWithHabits

@Dao
interface DaysOfWeekDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDays(days: List<Days_Of_Week>)

    @Query("SELECT * FROM days_of_week")
    fun getAllDays(): List<Days_Of_Week>

    @Transaction
    @Query("Select * from days_of_week where dayId = :dayId")
    suspend fun getDayWithHabits(dayId: Int): daysWithHabits

    @Transaction
    @Query("Select * from days_of_week")
    suspend fun getAllDaysWithHabits(): List<daysWithHabits>
}