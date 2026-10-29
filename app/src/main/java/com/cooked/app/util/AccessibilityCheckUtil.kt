package com.cooked.app.util

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.text.TextUtils

object AccessibilityCheckUtil {

    /**
     * Checks if the given accessibility [serviceClass] is currently enabled by the user.
     */
    fun isAccessibilityServiceEnabled(context: Context, serviceClass: Class<*>): Boolean {
        val expectedComponentName = ComponentName(context, serviceClass).flattenToString()
        val expectedShortName = ComponentName(context, serviceClass).flattenToShortString()
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        return isServiceTokenPresent(expectedComponentName, expectedShortName, enabledServices)
    }

    /**
     * Checks if the component name is present within the colon-separated enabled services string.
     * Uses TextUtils.SimpleStringSplitter as required, with JVM test compatibility fallback.
     */
    fun isServiceTokenPresent(
        expectedFullName: String,
        expectedShortName: String,
        enabledServices: String
    ): Boolean {
        if (enabledServices.isBlank()) return false
        try {
            val colonSplitter = TextUtils.SimpleStringSplitter(':')
            colonSplitter.setString(enabledServices)
            while (colonSplitter.hasNext()) {
                val componentName = colonSplitter.next()
                if (componentName.equals(expectedFullName, ignoreCase = true) ||
                    componentName.equals(expectedShortName, ignoreCase = true)
                ) {
                    return true
                }
            }
            return false
        } catch (_: Throwable) {
            return enabledServices.split(':').any {
                it.trim().equals(expectedFullName, ignoreCase = true) ||
                it.trim().equals(expectedShortName, ignoreCase = true)
            }
        }
    }
}
