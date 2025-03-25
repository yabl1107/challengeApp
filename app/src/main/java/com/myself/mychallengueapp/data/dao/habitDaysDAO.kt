package com.myself.mychallengueapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myself.mychallengueapp.data.model.Habit_Days

@Dao
interface habitDaysDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitDays: Habit_Days)

    @Query("Select * from habit_days")
    fun getAllHabitDays(): List<Habit_Days>

    @Delete
    suspend fun delete(habitDays: Habit_Days)
}