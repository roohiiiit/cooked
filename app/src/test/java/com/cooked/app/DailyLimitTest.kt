package com.cooked.app

import com.cooked.app.ui.onboarding.DailyLimitUiState
import com.cooked.app.ui.onboarding.DailyLimitViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class DailyLimitTest {

    @Test
    fun defaultUiState_hasExpectedDefaults() {
        val state = DailyLimitUiState()
        assertEquals(60, state.currentLimit)
        assertEquals(listOf(30, 60, 100, 150), state.presets)
    }

    @Test
    fun statusCaption_mapsCorrectlyAcrossAllThresholds() {
        assertEquals("Strict discipline 🧘", DailyLimitViewModel.getStatusCaption(10))
        assertEquals("Strict discipline 🧘", DailyLimitViewModel.getStatusCaption(30))
        assertEquals("Strict discipline 🧘", DailyLimitViewModel.getStatusCaption(39))

        assertEquals("Healthy balance ⚖️", DailyLimitViewModel.getStatusCaption(40))
        assertEquals("Healthy balance ⚖️", DailyLimitViewModel.getStatusCaption(60))
        assertEquals("Healthy balance ⚖️", DailyLimitViewModel.getStatusCaption(80))

        assertEquals("Playing with fire 🔥", DailyLimitViewModel.getStatusCaption(81))
        assertEquals("Playing with fire 🔥", DailyLimitViewModel.getStatusCaption(100))
        assertEquals("Playing with fire 🔥", DailyLimitViewModel.getStatusCaption(120))

        assertEquals("Deep fried behavior 💀", DailyLimitViewModel.getStatusCaption(121))
        assertEquals("Deep fried behavior 💀", DailyLimitViewModel.getStatusCaption(150))
        assertEquals("Deep fried behavior 💀", DailyLimitViewModel.getStatusCaption(250))
    }
}
