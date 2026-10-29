package com.cooked.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cooked.app.data.model.CountDisplayStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class CountStyleDataStore(private val context: Context) {

    companion object {
        val KEY_COUNT_DISPLAY_STYLE = stringPreferencesKey("count_display_style")
    }

    val countDisplayStyleFlow: Flow<CountDisplayStyle> = context.onboardingDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val stored = preferences[KEY_COUNT_DISPLAY_STYLE]
            CountDisplayStyle.fromStorageKey(stored)
        }

    suspend fun setCountDisplayStyle(style: CountDisplayStyle) {
        context.onboardingDataStore.edit { preferences ->
            preferences[KEY_COUNT_DISPLAY_STYLE] = style.storageKey
        }
    }
}
