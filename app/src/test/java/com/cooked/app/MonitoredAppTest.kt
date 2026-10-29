package com.cooked.app

import com.cooked.app.data.model.MonitoredApp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MonitoredAppTest {

    @Test
    fun verifyPackageNames() {
        assertEquals("com.instagram.android", MonitoredApp.INSTAGRAM.packageName)
        assertEquals("com.facebook.katana", MonitoredApp.FACEBOOK.packageName)
        assertEquals("com.snapchat.android", MonitoredApp.SNAPCHAT.packageName)
        assertEquals("com.twitter.android", MonitoredApp.X.packageName)
        assertEquals("com.reddit.frontpage", MonitoredApp.REDDIT.packageName)
        assertEquals("com.google.android.youtube", MonitoredApp.YOUTUBE.packageName)
    }

    @Test
    fun verifyAllSixAppsPresent() {
        val apps = MonitoredApp.allApps()
        assertEquals(6, apps.size)
        assertTrue(apps.any { it.displayName == "Instagram" })
        assertTrue(apps.any { it.displayName == "Facebook" })
        assertTrue(apps.any { it.displayName == "Snapchat" })
        assertTrue(apps.any { it.displayName == "X" })
        assertTrue(apps.any { it.displayName == "Reddit" })
        assertTrue(apps.any { it.displayName == "YouTube" })
    }
}
