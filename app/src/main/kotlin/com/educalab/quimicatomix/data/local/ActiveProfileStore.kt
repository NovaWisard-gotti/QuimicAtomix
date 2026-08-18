package com.educalab.quimicatomix.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.activeProfileDataStore by preferencesDataStore(name = "active_profile")
private val ACTIVE_PROFILE_ID = longPreferencesKey("active_profile_id")

/**
 * Persiste localmente qué perfil estaba activo, para recordarlo entre aperturas de la app
 * (antes solo se recuperaba heurísticamente el perfil "más recientemente activo" en la BD).
 */
class ActiveProfileStore(private val context: Context) {

    suspend fun read(): Long? = context.activeProfileDataStore.data.first()[ACTIVE_PROFILE_ID]

    suspend fun save(id: Long) {
        context.activeProfileDataStore.edit { prefs -> prefs[ACTIVE_PROFILE_ID] = id }
    }
}
