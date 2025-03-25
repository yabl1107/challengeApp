package com.myself.mychallengueapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.myself.mychallengueapp.data.model.HabitDC
import com.myself.mychallengueapp.data.relations.habitWIthDays
import kotlinx.coroutines.flow.Flow

@Dao
interface habitDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitDC)

    @Query("Select * from habit")
    fun getAllHabits(): Flow<List<HabitDC>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(habit: HabitDC)

    @Delete
    suspend fun delete(habit: HabitDC)

    @Transaction
    @Query("Select * from habit where habitId= :habitId")
    suspend fun getHabitWithDays(habitId: Int): habitWIthDays

    @Transaction
    @Query("Select * from habit")
    suspend fun getAllHabitWithDays(): habitWIthDays
}