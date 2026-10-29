package com.cooked.app.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cooked.app.data.CountStyleDataStore
import com.cooked.app.data.model.CountDisplayStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CountStyleViewModel(
    application: Application,
    private val dataStore: CountStyleDataStore
) : AndroidViewModel(application) {

    constructor(application: Application) : this(
        application,
        CountStyleDataStore(application)
    )

    private val _uiState = MutableStateFlow(CountDisplayStyle.NOTIFICATION_ONLY)
    val uiState: StateFlow<CountDisplayStyle> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val saved = dataStore.countDisplayStyleFlow.first()
            _uiState.value = saved
        }
    }

    fun onOptionSelected(style: CountDisplayStyle) {
        _uiState.value = style
    }

    fun saveAndContinue(onNavigateNext: () -> Unit) {
        viewModelScope.launch {
            dataStore.setCountDisplayStyle(_uiState.value)
            onNavigateNext()
        }
    }
}
