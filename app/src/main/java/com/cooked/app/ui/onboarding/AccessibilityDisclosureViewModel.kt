package com.cooked.app.ui.onboarding

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cooked.app.R
import com.cooked.app.service.CookedAccessibilityService
import com.cooked.app.util.AccessibilityCheckUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AccessibilityScreenEvent {
    data object NavigateNext : AccessibilityScreenEvent
    data class ShowSnackbar(val messageResId: Int) : AccessibilityScreenEvent
}

class AccessibilityDisclosureViewModel(
    private val serviceChecker: (Context) -> Boolean = { context ->
        AccessibilityCheckUtil.isAccessibilityServiceEnabled(
            context,
            CookedAccessibilityService::class.java
        )
    },
    externalScope: CoroutineScope? = null
) : ViewModel() {

    private val scope: CoroutineScope = externalScope ?: viewModelScope

    private val _hasRequestedSettings = MutableStateFlow(false)
    val hasRequestedSettings: StateFlow<Boolean> = _hasRequestedSettings.asStateFlow()

    private val _showNoThanksDialog = MutableStateFlow(false)
    val showNoThanksDialog: StateFlow<Boolean> = _showNoThanksDialog.asStateFlow()

    private val _events = MutableSharedFlow<AccessibilityScreenEvent>()
    val events: SharedFlow<AccessibilityScreenEvent> = _events.asSharedFlow()

    fun onAgreeClicked(context: Context) {
        _hasRequestedSettings.value = true
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    /**
     * Testing hook to simulate the user navigating to Settings.
     */
    fun markSettingsRequested() {
        _hasRequestedSettings.value = true
    }

    fun onNoThanksClicked() {
        _showNoThanksDialog.value = true
    }

    fun onDismissNoThanksDialog() {
        _showNoThanksDialog.value = false
    }

    fun onResumeCheck(context: Context) {
        val isEnabled = serviceChecker(context)
        if (isEnabled) {
            scope.launch {
                _events.emit(AccessibilityScreenEvent.NavigateNext)
            }
        } else if (_hasRequestedSettings.value) {
            _hasRequestedSettings.value = false
            scope.launch {
                _events.emit(AccessibilityScreenEvent.ShowSnackbar(R.string.screen6_snackbar_disabled))
            }
        }
    }
}
