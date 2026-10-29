package com.cooked.app.data.model

enum class MonitoredApp(
    val displayName: String,
    val packageName: String
) {
    INSTAGRAM(displayName = "Instagram", packageName = "com.instagram.android"),
    FACEBOOK(displayName = "Facebook", packageName = "com.facebook.katana"),
    SNAPCHAT(displayName = "Snapchat", packageName = "com.snapchat.android"),
    X(displayName = "X", packageName = "com.twitter.android"),
    REDDIT(displayName = "Reddit", packageName = "com.reddit.frontpage"),
    YOUTUBE(displayName = "YouTube", packageName = "com.google.android.youtube");

    companion object {
        fun allApps(): List<MonitoredApp> = entries.toList()
    }
}
