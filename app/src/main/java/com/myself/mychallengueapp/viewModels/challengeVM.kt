package com.myself.mychallengueapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myself.mychallengueapp.data.model.ChallengeDC
import com.myself.mychallengueapp.data.repository.challengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class challengeVM @Inject constructor(private val repository: challengeRepository): ViewModel(){

    private val _challenges = MutableStateFlow<List<ChallengeDC>>(emptyList())
    val challenges: StateFlow<List<ChallengeDC>> = _challenges.asStateFlow()

    init {
        getAllChallenges()
    }

    fun getAllChallenges() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllChallenges().collect { challenges ->
                if(challenges.isNullOrEmpty()){
                    _challenges.value = emptyList()
                }else{
                    _challenges.value = challenges
                }
            }
        }
    }
}