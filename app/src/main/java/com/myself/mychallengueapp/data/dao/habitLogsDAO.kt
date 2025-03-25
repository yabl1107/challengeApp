package com.myself.mychallengueapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myself.mychallengueapp.data.model.habitLogs

@Dao
interface habitLogsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habitLog: habitLogs)

    @Query("Select * from habit_logs")
    fun getAllHabitLogs(): List<habitLogs>

}