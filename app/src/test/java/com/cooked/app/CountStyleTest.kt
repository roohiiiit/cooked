package com.cooked.app

import com.cooked.app.data.model.CountDisplayStyle
import org.junit.Assert.assertEquals
import org.junit.Test

class CountStyleTest {

    @Test
    fun countDisplayStyle_storageKeys_matchExpectedValues() {
        assertEquals("notification_only", CountDisplayStyle.NOTIFICATION_ONLY.storageKey)
        assertEquals("notification_milestones", CountDisplayStyle.NOTIFICATION_MILESTONES.storageKey)
        assertEquals("floating_counter", CountDisplayStyle.FLOATING_COUNTER.storageKey)
        assertEquals("hidden_stats_only", CountDisplayStyle.HIDDEN_STATS_ONLY.storageKey)
    }

    @Test
    fun fromStorageKey_returnsCorrectEnum() {
        assertEquals(CountDisplayStyle.NOTIFICATION_ONLY, CountDisplayStyle.fromStorageKey("notification_only"))
        assertEquals(CountDisplayStyle.NOTIFICATION_MILESTONES, CountDisplayStyle.fromStorageKey("notification_milestones"))
        assertEquals(CountDisplayStyle.FLOATING_COUNTER, CountDisplayStyle.fromStorageKey("floating_counter"))
        assertEquals(CountDisplayStyle.HIDDEN_STATS_ONLY, CountDisplayStyle.fromStorageKey("hidden_stats_only"))
    }

    @Test
    fun fromStorageKey_withUnknownOrNull_fallsBackToNotificationOnly() {
        assertEquals(CountDisplayStyle.NOTIFICATION_ONLY, CountDisplayStyle.fromStorageKey(null))
        assertEquals(CountDisplayStyle.NOTIFICATION_ONLY, CountDisplayStyle.fromStorageKey("unknown_style"))
    }
}
