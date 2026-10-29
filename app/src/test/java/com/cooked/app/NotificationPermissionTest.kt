package com.cooked.app

import com.cooked.app.data.model.CountDisplayStyle
import com.cooked.app.ui.onboarding.NotificationPermissionViewModel
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationPermissionTest {

    @Test
    fun shouldBypassForStyle_whenHiddenStatsOnly_returnsTrue() {
        assertTrue(
            NotificationPermissionViewModel.shouldBypassForStyle(
                CountDisplayStyle.HIDDEN_STATS_ONLY
            )
        )
    }

    @Test
    fun shouldBypassForStyle_whenOtherStyles_returnsFalse() {
        assertFalse(
            NotificationPermissionViewModel.shouldBypassForStyle(
                CountDisplayStyle.NOTIFICATION_ONLY
            )
        )
        assertFalse(
            NotificationPermissionViewModel.shouldBypassForStyle(
                CountDisplayStyle.NOTIFICATION_MILESTONES
            )
        )
        assertFalse(
            NotificationPermissionViewModel.shouldBypassForStyle(
                CountDisplayStyle.FLOATING_COUNTER
            )
        )
    }
}
