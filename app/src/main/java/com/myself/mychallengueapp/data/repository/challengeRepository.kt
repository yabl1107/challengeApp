package com.myself.mychallengueapp.data.repository

import com.myself.mychallengueapp.data.dao.challengeDAO
import com.myself.mychallengueapp.data.model.ChallengeDC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class challengeRepository @Inject constructor(private val challengeDAO: challengeDAO){
    suspend fun addChallengue(challenge: ChallengeDC) = challengeDAO.insert(challenge)
    suspend fun updateChallengue(challenge: ChallengeDC) = challengeDAO.update(challenge)
    suspend fun deleteChallengue(challenge: ChallengeDC) = challengeDAO.delete(challenge)

    fun getAllChallenges(): Flow<List<ChallengeDC>> = challengeDAO.getAllChallenges().flowOn(
        Dispatchers.IO).conflate()
}