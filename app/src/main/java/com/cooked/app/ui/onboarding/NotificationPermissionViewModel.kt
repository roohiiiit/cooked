package com.cooked.app.ui.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cooked.app.data.CountStyleDataStore
import com.cooked.app.data.model.CountDisplayStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NotificationPermissionViewModel(
    application: Application,
    private val styleFlowProvider: () -> Flow<CountDisplayStyle>
) : AndroidViewModel(application) {

    constructor(application: Application) : this(
        application,
        { CountStyleDataStore(application).countDisplayStyleFlow }
    )

    private val _shouldBypass = MutableStateFlow(false)
    val shouldBypass: StateFlow<Boolean> = _shouldBypass.asStateFlow()

    init {
        viewModelScope.launch {
            val style = styleFlowProvider().first()
            _shouldBypass.value = shouldBypassForStyle(style)
        }
    }

    companion object {
        fun shouldBypassForStyle(style: CountDisplayStyle): Boolean {
            return style == CountDisplayStyle.HIDDEN_STATS_ONLY
        }
    }
}
