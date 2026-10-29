package com.cooked.app.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * Accessibility service for detecting scroll events and feed states
 * in user-selected social media applications.
 */
class CookedAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Will process scroll events for picked apps
    }

    override fun onInterrupt() {
        // Handle interruption or cancellation of feedback
    }
}
