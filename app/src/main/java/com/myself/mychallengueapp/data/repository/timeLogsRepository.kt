package com.myself.mychallengueapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.myself.mychallengueapp.data.datastore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class timeLogsRepository  @Inject constructor(
    @ApplicationContext private val context: Context
){
    // Guardar el valor del temporizador en el DataStore
    suspend fun saveTimerValue(habitId: Int, date: LocalDate, value: Long) {
        context.datastore.edit { preferences ->
            preferences[habitTimerKey(habitId, date)] = value
        }
    }

    // Obtener el valor guardado del DataStore
    fun getTimerValue(habitId: Int, date: LocalDate): Flow<Long> {
        return context.datastore.data.map { preferences ->
                preferences[habitTimerKey(habitId, date)] ?: 0L // Si no existe, retornar 0L
            }
    }

    suspend fun getTimerValueSync(habitId: Int, date: LocalDate): Long {
        val preferences = context.datastore.data.first() // Obtén los datos de forma bloqueante
        return preferences[habitTimerKey(habitId, date)] ?: 0L // Si no existe, retornar 0L
    }

    // Crear la clave para el habitId y la fecha
    private fun habitTimerKey(habitId: Int, date: LocalDate): Preferences.Key<Long> {
        return longPreferencesKey("${habitId}_$date")
    }

    suspend fun clearAllTimers() {
        context.datastore.edit { preferences ->
            preferences.clear()  // Esto borra todas las claves y valores en el datastore
        }
    }

    suspend fun deleteTimerValue(habitId: Int, selectedDate: LocalDate) {
        context.datastore.edit { preferences ->
            preferences.remove(habitTimerKey(habitId, selectedDate))
        }
    }

    suspend fun setLastDate(date: LocalDate){
        context.datastore.edit { preferences ->
            preferences[longPreferencesKey("lastDate")] = date.toEpochDay()
        }
    }

    suspend fun getLastDate(): Long {
        val preferences = context.datastore.data.first() // Obtén los datos de forma bloqueante
        return preferences[longPreferencesKey("lastDate")] ?: 0L
    }
}