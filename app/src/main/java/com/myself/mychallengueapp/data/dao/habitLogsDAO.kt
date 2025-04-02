package com.myself.mychallengueapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myself.mychallengueapp.data.model.habitLogs
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface habitLogsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitLog: habitLogs)

    @Query("Select * from habit_logs")
    fun getAllHabitLogs(): Flow<List<habitLogs>>

    @Query("Delete from habit_logs where habitId = :habitId and date = :date")
    suspend fun delete(habitId: Int, date: LocalDate)
}