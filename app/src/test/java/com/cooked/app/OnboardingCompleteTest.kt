package com.cooked.app

import com.cooked.app.data.OnboardingPreferencesRepository
import com.cooked.app.data.model.CountDisplayStyle
import com.cooked.app.ui.onboarding.OnboardingFinishViewModel
import com.cooked.app.ui.onboarding.OnboardingSummaryUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingCompleteTest {

    @Test
    fun onboardingPreferencesKeys_matchSpecifications() {
        assertEquals("onboarding_done", OnboardingPreferencesRepository.KEY_ONBOARDING_DONE.name)
        assertEquals("monitored_app_packages", OnboardingPreferencesRepository.KEY_MONITORED_APPS.name)
        assertEquals("daily_scroll_limit", OnboardingPreferencesRepository.KEY_DAILY_LIMIT.name)
        assertEquals("count_display_style", OnboardingPreferencesRepository.KEY_COUNT_STYLE.name)
        assertEquals(60, OnboardingPreferencesRepository.DEFAULT_DAILY_LIMIT)
    }

    @Test
    fun defaultUiState_matchesSpecifications() {
        val defaultState = OnboardingSummaryUiState()
        assertEquals(0, defaultState.monitoredAppCount)
        assertTrue(defaultState.monitoredAppNames.isEmpty())
        assertTrue(defaultState.monitoredAppPackages.isEmpty())
        assertEquals(60, defaultState.dailyLimit)
        assertEquals("Notification only", defaultState.countStyleTitle)
        assertTrue(defaultState.isLoading)
    }

    @Test
    fun formatCountStyleTitle_mapsAllStylesCorrectly() {
        assertEquals(
            "Notification only",
            OnboardingFinishViewModel.formatCountStyleTitle(CountDisplayStyle.NOTIFICATION_ONLY)
        )
        assertEquals(
            "Notification + milestones",
            OnboardingFinishViewModel.formatCountStyleTitle(CountDisplayStyle.NOTIFICATION_MILESTONES)
        )
        assertEquals(
            "Floating counter",
            OnboardingFinishViewModel.formatCountStyleTitle(CountDisplayStyle.FLOATING_COUNTER)
        )
        assertEquals(
            "Hidden, stats only",
            OnboardingFinishViewModel.formatCountStyleTitle(CountDisplayStyle.HIDDEN_STATS_ONLY)
        )
    }
}
