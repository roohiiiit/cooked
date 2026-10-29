package com.cooked.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cooked.app.data.CountStyleDataStore
import com.cooked.app.data.OnboardingPreferencesRepository
import com.cooked.app.data.model.CountDisplayStyle
import com.cooked.app.ui.components.CookedPrimaryButton
import com.cooked.app.ui.onboarding.AccessibilityDisclosureScreen
import com.cooked.app.ui.onboarding.AddWidgetOnboardingScreen
import com.cooked.app.ui.onboarding.CountStyleScreen
import com.cooked.app.ui.onboarding.HowItWorksScreen
import com.cooked.app.ui.onboarding.DailyLimitScreen
import com.cooked.app.ui.onboarding.NotificationPermissionScreen
import com.cooked.app.ui.onboarding.OnboardingCompleteScreen
import com.cooked.app.ui.onboarding.PickAppsScreen
import com.cooked.app.ui.onboarding.WelcomeScreen
import com.cooked.app.ui.theme.CookedBackground
import com.cooked.app.ui.theme.CookedMutedGrey
import com.cooked.app.ui.theme.CookedPrimaryText
import com.cooked.app.ui.theme.CookedTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookedTheme {
                CookedNavGraph()
            }
        }
    }
}

@Composable
fun CookedNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val countStyleDataStore = remember { CountStyleDataStore(context) }
    val onboardingPreferencesRepository = remember { OnboardingPreferencesRepository(context) }

    var isCheckingOnboarding by remember { mutableStateOf(true) }
    var startRoute by remember { mutableStateOf("welcome") }

    LaunchedEffect(Unit) {
        val completed = onboardingPreferencesRepository.isOnboardingCompletedFlow.first()
        startRoute = if (completed) "dashboard" else "welcome"
        isCheckingOnboarding = false
    }

    if (isCheckingOnboarding) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CookedBackground)
        )
        return
    }

    fun navigateAfterAccessibility() {
        coroutineScope.launch {
            val style = countStyleDataStore.countDisplayStyleFlow.first()
            if (style == CountDisplayStyle.HIDDEN_STATS_ONLY) {
                navController.navigate("daily_limit")
            } else {
                navController.navigate("notification_permission")
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        composable("welcome") {
            WelcomeScreen(
                onContinue = {
                    navController.navigate("how_it_works")
                }
            )
        }
        composable("how_it_works") {
            HowItWorksScreen(
                onNext = {
                    navController.navigate("pick_apps")
                },
                onSkip = {
                    navController.navigate("pick_apps")
                }
            )
        }
        composable("pick_apps") {
            PickAppsScreen(
                onContinue = {
                    navController.navigate("add_widget")
                }
            )
        }
        composable("add_widget") {
            AddWidgetOnboardingScreen(
                onContinue = {
                    navController.navigate("count_style")
                },
                onSkip = {
                    navController.navigate("count_style")
                }
            )
        }
        composable("count_style") {
            CountStyleScreen(
                onNext = {
                    navController.navigate("accessibility_disclosure")
                }
            )
        }
        composable("accessibility_disclosure") {
            AccessibilityDisclosureScreen(
                onNavigateNext = {
                    navigateAfterAccessibility()
                }
            )
        }
        composable("screen_6_placeholder") {
            AccessibilityDisclosureScreen(
                onNavigateNext = {
                    navigateAfterAccessibility()
                }
            )
        }
        composable("notification_permission") {
            NotificationPermissionScreen(
                onNavigateNext = {
                    navController.navigate("daily_limit")
                }
            )
        }
        composable("screen_7_placeholder") {
            NotificationPermissionScreen(
                onNavigateNext = {
                    navController.navigate("daily_limit")
                }
            )
        }
        composable("daily_limit") {
            DailyLimitScreen(
                onNavigateNext = {
                    navController.navigate("onboarding_complete")
                }
            )
        }
        composable("screen_8_placeholder") {
            DailyLimitScreen(
                onNavigateNext = {
                    navController.navigate("onboarding_complete")
                }
            )
        }
        composable("onboarding_complete") {
            OnboardingCompleteScreen(
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("welcome") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("screen_9_placeholder") {
            OnboardingCompleteScreen(
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("welcome") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("dashboard") {
            DashboardScreen()
        }
    }
}

@Composable
fun Screen8Placeholder(
    onNavigateNext: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CookedBackground)
            .clickable { onNavigateNext() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.screen_8_placeholder),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = CookedPrimaryText
                )
            )
            CookedPrimaryButton(
                text = stringResource(id = R.string.button_next),
                onClick = onNavigateNext,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun DashboardScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CookedBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.dashboard_title),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = CookedPrimaryText,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "Welcome to Cooked!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CookedMutedGrey
                )
            )
        }
    }
}
