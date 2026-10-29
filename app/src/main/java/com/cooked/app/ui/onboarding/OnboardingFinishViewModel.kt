package com.cooked.app.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cooked.app.data.OnboardingPreferencesRepository
import com.cooked.app.data.model.CountDisplayStyle
import com.cooked.app.data.model.MonitoredApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class OnboardingFinishViewModel(
    application: Application,
    private val dataStoreRepository: OnboardingPreferencesRepository
) : AndroidViewModel(application) {

    constructor(application: Application) : this(
        application,
        OnboardingPreferencesRepository(application)
    )

    private val _uiState = MutableStateFlow(OnboardingSummaryUiState())
    val uiState: StateFlow<OnboardingSummaryUiState> = _uiState.asStateFlow()

    init {
        loadSummary()
    }

    private fun loadSummary() {
        viewModelScope.launch {
            combine(
                dataStoreRepository.monitoredAppsFlow,
                dataStoreRepository.dailyLimitFlow,
                dataStoreRepository.countStyleFlow
            ) { apps, limit, style ->
                val appNames = apps.map { pkg ->
                    MonitoredApp.entries.firstOrNull { it.packageName == pkg }?.displayName ?: pkg
                }.sorted()

                OnboardingSummaryUiState(
                    monitoredAppCount = apps.size,
                    monitoredAppNames = appNames,
                    monitoredAppPackages = apps.toList().sorted(),
                    dailyLimit = limit,
                    countStyleTitle = formatCountStyleTitle(style),
                    isLoading = false
                )
            }.collect { summaryState ->
                _uiState.value = summaryState
            }
        }
    }

    fun completeOnboarding(onNavigateToDashboard: () -> Unit) {
        viewModelScope.launch {
            dataStoreRepository.setOnboardingCompleted(true)
            onNavigateToDashboard()
        }
    }

    companion object {
        fun formatCountStyleTitle(style: CountDisplayStyle): String = when (style) {
            CountDisplayStyle.NOTIFICATION_ONLY -> "Notification only"
            CountDisplayStyle.NOTIFICATION_MILESTONES -> "Notification + milestones"
            CountDisplayStyle.FLOATING_COUNTER -> "Floating counter"
            CountDisplayStyle.HIDDEN_STATS_ONLY -> "Hidden, stats only"
        }
    }
}

typealias OnboardingSummaryViewModel = OnboardingFinishViewModel
