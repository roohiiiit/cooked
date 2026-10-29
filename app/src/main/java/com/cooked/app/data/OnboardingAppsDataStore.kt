package com.cooked.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_preferences")

class OnboardingAppsDataStore(private val context: Context) {

    companion object {
        val KEY_MONITORED_APP_PACKAGES = stringSetPreferencesKey("monitored_app_packages")
    }

    val monitoredAppsFlow: Flow<Set<String>> = context.onboardingDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_MONITORED_APP_PACKAGES] ?: emptySet()
        }

    suspend fun saveMonitoredApps(packages: Set<String>) {
        context.onboardingDataStore.edit { preferences ->
            preferences[KEY_MONITORED_APP_PACKAGES] = packages
        }
    }
}
