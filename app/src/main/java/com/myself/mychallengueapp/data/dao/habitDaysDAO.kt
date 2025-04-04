package com.myself.mychallengueapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.model.Habit_Days
import kotlinx.coroutines.flow.Flow

@Dao
interface habitDaysDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitDays: Habit_Days)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(habitDays: List<Habit_Days>)

    @Query("Select * from habit_days")
    fun getAllHabitDays(): Flow<List<Habit_Days>>

    @Query("Select * from habit_days where dayId = :dayId")
    fun getHabitDaysByDayId(dayId: Int): List<Habit_Days>

    @Delete
    suspend fun delete(habitDays: Habit_Days)

    @Query("DELETE FROM habit_days WHERE habitId = :habitId")
    suspend fun deleteByHabitId(habitId: Int)

}