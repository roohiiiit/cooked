package com.cooked.app.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cooked.app.data.OnboardingPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DailyLimitUiState(
    val currentLimit: Int = 60,
    val presets: List<Int> = listOf(30, 60, 100, 150),
    val isLoading: Boolean = true
)

class DailyLimitViewModel(
    application: Application,
    private val repository: OnboardingPreferencesRepository
) : AndroidViewModel(application) {

    constructor(application: Application) : this(
        application,
        OnboardingPreferencesRepository(application)
    )

    private val _uiState = MutableStateFlow(DailyLimitUiState())
    val uiState: StateFlow<DailyLimitUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val savedLimit = repository.dailyLimitFlow.first()
            _uiState.update {
                it.copy(
                    currentLimit = savedLimit,
                    isLoading = false
                )
            }
        }
    }

    fun onLimitChanged(newLimit: Int) {
        val clamped = newLimit.coerceIn(10, 250)
        _uiState.update { it.copy(currentLimit = clamped) }
    }

    fun saveAndContinue(onNavigateNext: () -> Unit) {
        viewModelScope.launch {
            repository.setDailyLimit(_uiState.value.currentLimit)
            onNavigateNext()
        }
    }

    companion object {
        fun getStatusCaption(limit: Int): String = when {
            limit < 40 -> "Strict discipline 🧘"
            limit <= 80 -> "Healthy balance ⚖️"
            limit <= 120 -> "Playing with fire 🔥"
            else -> "Deep fried behavior 💀"
        }
    }
}
