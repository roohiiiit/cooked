package com.cooked.app.data.model

/**
 * 4 Display options for how doomscroll counts are surfaced to the user.
 */
enum class CountDisplayStyle(val storageKey: String) {
    NOTIFICATION_ONLY("notification_only"),
    NOTIFICATION_MILESTONES("notification_milestones"),
    FLOATING_COUNTER("floating_counter"),
    HIDDEN_STATS_ONLY("hidden_stats_only");

    companion object {
        fun fromStorageKey(key: String?): CountDisplayStyle {
            return entries.firstOrNull { it.storageKey == key } ?: NOTIFICATION_ONLY
        }
    }
}
