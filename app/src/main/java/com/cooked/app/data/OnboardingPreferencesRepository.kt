package com.cooked.app.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.cooked.app.data.model.CountDisplayStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class OnboardingPreferencesRepository(private val context: Context) {

    companion object {
        val KEY_ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val KEY_MONITORED_APPS = stringSetPreferencesKey("monitored_app_packages")
        val KEY_DAILY_LIMIT = intPreferencesKey("daily_scroll_limit")
        val KEY_COUNT_STYLE = stringPreferencesKey("count_display_style")

        const val DEFAULT_DAILY_LIMIT = 60
    }

    val isOnboardingCompletedFlow: Flow<Boolean> = context.onboardingDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[KEY_ONBOARDING_DONE] ?: false
        }

    val monitoredAppsFlow: Flow<Set<String>> = context.onboardingDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[KEY_MONITORED_APPS] ?: emptySet()
        }

    val dailyLimitFlow: Flow<Int> = context.onboardingDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[KEY_DAILY_LIMIT] ?: DEFAULT_DAILY_LIMIT
        }

    val countStyleFlow: Flow<CountDisplayStyle> = context.onboardingDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val stored = preferences[KEY_COUNT_STYLE]
            CountDisplayStyle.fromStorageKey(stored)
        }

    suspend fun setOnboardingCompleted(completed: Boolean = true) {
        context.onboardingDataStore.edit { preferences ->
            preferences[KEY_ONBOARDING_DONE] = completed
        }
    }

    suspend fun setDailyLimit(limit: Int) {
        context.onboardingDataStore.edit { preferences ->
            preferences[KEY_DAILY_LIMIT] = limit
        }
    }

    suspend fun setMonitoredApps(packages: Set<String>) {
        context.onboardingDataStore.edit { preferences ->
            preferences[KEY_MONITORED_APPS] = packages
        }
    }

    suspend fun setCountDisplayStyle(style: CountDisplayStyle) {
        context.onboardingDataStore.edit { preferences ->
            preferences[KEY_COUNT_STYLE] = style.storageKey
        }
    }
}
