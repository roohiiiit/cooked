package com.cooked.app.ui.onboarding

data class OnboardingSummaryUiState(
    val monitoredAppCount: Int = 0,
    val monitoredAppNames: List<String> = emptyList(),
    val monitoredAppPackages: List<String> = emptyList(),
    val dailyLimit: Int = 60,
    val countStyleTitle: String = "Notification only",
    val isLoading: Boolean = true
)
