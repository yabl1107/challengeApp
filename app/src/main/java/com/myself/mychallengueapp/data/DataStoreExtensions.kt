package com.myself.mychallengueapp.data

// En DataStoreExtensions.kt o un archivo similar
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

// Esta es la extensión para acceder al datastore desde el contexto
val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "todayTimers")
