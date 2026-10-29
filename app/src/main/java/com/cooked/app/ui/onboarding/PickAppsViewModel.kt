package com.cooked.app.ui.onboarding

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cooked.app.data.OnboardingAppsDataStore
import com.cooked.app.data.model.MonitoredApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class InstalledAppItem(
    val app: MonitoredApp,
    val icon: ImageBitmap? = null
)

class PickAppsViewModel(
    application: Application,
    private val dataStore: OnboardingAppsDataStore
) : AndroidViewModel(application) {

    constructor(application: Application) : this(
        application,
        OnboardingAppsDataStore(application)
    )

    private val _installedApps = MutableStateFlow<List<InstalledAppItem>>(emptyList())
    val installedApps: StateFlow<List<InstalledAppItem>> = _installedApps.asStateFlow()

    private val _selectedApps = MutableStateFlow<Set<String>>(emptySet())
    val selectedApps: StateFlow<Set<String>> = _selectedApps.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadInstalledAppsAndSelections()
    }

    fun loadInstalledAppsAndSelections() {
        viewModelScope.launch {
            _isLoading.value = true

            // Discover installed apps and their icons on IO thread
            val installed = withContext(Dispatchers.IO) {
                queryInstalledMonitoredApps(getApplication())
            }
            _installedApps.value = installed

            // Pre-populate selections from DataStore, only keeping valid installed apps
            val savedSelections = dataStore.monitoredAppsFlow.first()
            val validInstalledPackages = installed.map { it.app.packageName }.toSet()
            val filteredSaved = savedSelections.filter { it in validInstalledPackages }.toSet()

            if (filteredSaved.isNotEmpty()) {
                _selectedApps.value = filteredSaved
            }

            _isLoading.value = false
        }
    }

    fun toggleApp(packageName: String) {
        _selectedApps.update { current ->
            if (packageName in current) {
                current - packageName
            } else {
                current + packageName
            }
        }
    }

    fun onContinueClicked(onSuccess: () -> Unit) {
        val appsToSave = _selectedApps.value
        if (appsToSave.isNotEmpty()) {
            viewModelScope.launch {
                dataStore.saveMonitoredApps(appsToSave)
                onSuccess()
            }
        }
    }

    companion object {
        fun queryInstalledMonitoredApps(context: Context): List<InstalledAppItem> {
            val pm = context.packageManager
            val result = mutableListOf<InstalledAppItem>()

            for (app in MonitoredApp.allApps()) {
                try {
                    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        pm.getPackageInfo(app.packageName, PackageManager.PackageInfoFlags.of(0))
                    } else {
                        @Suppress("DEPRECATION")
                        pm.getPackageInfo(app.packageName, 0)
                    }

                    val appInfo = packageInfo.applicationInfo ?: continue
                    val drawable: Drawable = appInfo.loadIcon(pm)
                    val bitmap = drawableToBitmap(drawable)
                    result.add(
                        InstalledAppItem(
                            app = app,
                            icon = bitmap?.asImageBitmap()
                        )
                    )
                } catch (_: PackageManager.NameNotFoundException) {
                    // App is not installed on device, skip it
                }
            }
            return result
        }

        private fun drawableToBitmap(drawable: Drawable): Bitmap? {
            return try {
                drawable.toBitmap(width = 96, height = 96, config = Bitmap.Config.ARGB_8888)
            } catch (_: Exception) {
                try {
                    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 96
                    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 96
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    bitmap
                } catch (_: Exception) {
                    null
                }
            }
        }
    }
}
