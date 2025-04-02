package com.myself.mychallengueapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.myself.mychallengueapp.data.model.ChallengeDC
import com.myself.mychallengueapp.data.relations.ChallengeWithHabits
import kotlinx.coroutines.flow.Flow

@Dao
interface challengeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(challenge: ChallengeDC): Long

    @Query("Select * from challenge")
    fun getAllChallenges(): Flow<List<ChallengeDC>>

    @Update
    suspend fun update(challenge: ChallengeDC)

    @Delete
    suspend fun delete(challenge: ChallengeDC)

    @Transaction
    @Query("Select * from challenge where challengeId = :challengeId")
    suspend fun getChallengeWithHabits(challengeId: Long): ChallengeWithHabits

    @Query("Select * from challenge")
    fun getAllChallengesWithHabits(): List<ChallengeWithHabits>

}