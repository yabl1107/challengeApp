package com.myself.mychallengueapp.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myself.mychallengueapp.data.relations.ChallengeWithHabits
import com.myself.mychallengueapp.data.repository.challengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class challengeDetailVM @Inject constructor(
    private val repository: challengeRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){


    private val _challengeState = MutableStateFlow<ChallengeWithHabits?>(null)
    val challengeState: StateFlow<ChallengeWithHabits?> = _challengeState

    init {
        val id = savedStateHandle.get<String>("id")?.toLong()
        id?.let {
            viewModelScope.launch (Dispatchers.IO){
                try {
                    _challengeState.value = repository.getChallengeWithHabits(it)
                    Log.d("ChallengeDetailVM", "Datos cargados: ${_challengeState.value}")
                } catch (e: Exception) {
                    Log.e("ChallengeDetailVM", "Error obteniendo datos: ${e.message}")
                }
            }
        }
    }


}