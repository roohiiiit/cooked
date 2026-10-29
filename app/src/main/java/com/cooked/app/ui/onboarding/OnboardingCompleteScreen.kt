package com.cooked.app.ui.onboarding

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cooked.app.R
import com.cooked.app.data.model.MonitoredApp
import com.cooked.app.ui.components.CookedPrimaryButton
import com.cooked.app.ui.components.CookedSegmentedProgressBar
import com.cooked.app.ui.components.ZonkAvatarHeader
import com.cooked.app.ui.theme.CookedBackground
import com.cooked.app.ui.theme.CookedCardBorder
import com.cooked.app.ui.theme.CookedFlameOrange
import com.cooked.app.ui.theme.CookedMutedGrey
import com.cooked.app.ui.theme.CookedPrimaryText
import com.cooked.app.ui.theme.CookedSecondaryText
import com.cooked.app.ui.theme.CookedSurface
import com.cooked.app.ui.theme.CookedTheme
import com.cooked.app.ui.theme.CookedTypography
import com.cooked.app.ui.theme.Dimens
import com.cooked.app.ui.theme.JetBrainsMonoFontFamily

/**
 * Stateful Composable for Screen 9 ("Final Onboarding Completion / Locked In").
 */
@Composable
fun OnboardingCompleteScreen(
    onNavigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingFinishViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OnboardingCompleteContent(
        state = uiState,
        onStartClicked = {
            viewModel.completeOnboarding(onNavigateToDashboard)
        },
        modifier = modifier
    )
}

/**
 * Stateless Composable for Screen 9 ("Final Onboarding Completion / Locked In" - Duolingo-style redesign).
 */
@Composable
fun OnboardingCompleteContent(
    state: OnboardingSummaryUiState,
    onStartClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CookedBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Elevated Segmented Capsule Progress Bar (Step 9 of 9 - 100% full)
        CookedSegmentedProgressBar(currentStep = 9)

        // 2. Duolingo-style Zonk Avatar + Speech Bubble Dialogue (Prominent & Visible)
        ZonkAvatarHeader(
            avatarRes = R.drawable.zonk_recovered,
            dialogueTitle = stringResource(id = R.string.screen9_headline),
            dialogueText = stringResource(id = R.string.screen9_subheadline),
            contentDescription = stringResource(id = R.string.screen9_zonk_content_desc),
            avatarSize = 92.dp,
            modifier = Modifier
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 10.dp, bottom = 12.dp)
        )

        // 3. Center Interactive Section (Centered in remaining viewport)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = Dimens.ScreenSideMargin),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Elevated Setup Summary Card
                ElevatedSetupSummaryCard(state = state)

                // Reassurance footer note
                Text(
                    text = "Tracking begins immediately in your selected feeds. You’re in complete control.",
                    style = CookedTypography.caption,
                    color = CookedMutedGrey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        // 4. Primary Bottom Action ("Start")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 12.dp, bottom = Dimens.ButtonBottomInsetPadding)
        ) {
            CookedPrimaryButton(
                text = stringResource(id = R.string.screen9_button_start),
                onClick = onStartClicked,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Elevated Summary Card featuring Gradient Surface, Native App Icon Badges, and Orange Pill Cap.
 */
@Composable
private fun ElevatedSetupSummaryCard(
    state: OnboardingSummaryUiState,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(20.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CookedSurface,
                        Color(0xFF0F1116)
                    )
                )
            )
            .border(1.dp, CookedCardBorder, cardShape)
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Row 1 - Monitored Feeds (STRICT: Circular icons only, NO text names)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.screen9_apps_picked_label),
                    style = CookedTypography.dialogueText.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = CookedMutedGrey
                    )
                )

                MonitoredAppIconsRow(packageNames = state.monitoredAppPackages)
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFF1E222A)
            )

            // Row 2 - Daily Cap (High-contrast orange pill badge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.screen9_daily_limit_label),
                    style = CookedTypography.dialogueText.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = CookedMutedGrey
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 50))
                        .background(Color(0x22FF5722))
                        .border(1.dp, CookedFlameOrange, RoundedCornerShape(percent = 50))
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.screen9_reels_badge_format, state.dailyLimit),
                        style = CookedTypography.monoCaption.copy(
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CookedFlameOrange
                        )
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFF1E222A)
            )

            // Row 3 - Alert Mode
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.screen9_count_style_label),
                    style = CookedTypography.dialogueText.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = CookedMutedGrey
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AlertModeIcon()
                    Text(
                        text = state.countStyleTitle,
                        style = CookedTypography.dialogueTitle.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CookedPrimaryText
                        )
                    )
                }
            }
        }
    }
}

/**
 * Renders ONLY the circular app icons side-by-side in an overlapping badge row.
 * Strictly NO text names.
 */
@Composable
private fun MonitoredAppIconsRow(
    packageNames: List<String>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pm = remember { context.packageManager }

    if (packageNames.isEmpty()) {
        Box(
            modifier = modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF22262E))
                .border(1.5.dp, CookedCardBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(CookedMutedGrey)
            )
        }
        return
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy((-6).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        packageNames.forEach { pkg ->
            AppIconBadge(
                packageName = pkg,
                packageManager = pm
            )
        }
    }
}

@Composable
private fun AppIconBadge(
    packageName: String,
    packageManager: PackageManager
) {
    val iconBitmap = remember(packageName) {
        loadAppIconBitmap(packageManager, packageName)
    }

    val monitoredApp = remember(packageName) {
        MonitoredApp.entries.firstOrNull { it.packageName == packageName }
    }

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .border(1.5.dp, Color(0xFF0F1116), CircleShape)
            .background(Color(0xFF1E2228)),
        contentAlignment = Alignment.Center
    ) {
        if (iconBitmap != null) {
            Image(
                bitmap = iconBitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Elegant fallback branded monogram
            val brandColor = when (monitoredApp) {
                MonitoredApp.INSTAGRAM -> Color(0xFFE1306C)
                MonitoredApp.YOUTUBE -> Color(0xFFFF0000)
                MonitoredApp.REDDIT -> Color(0xFFFF4500)
                MonitoredApp.SNAPCHAT -> Color(0xFFFFFC00)
                MonitoredApp.FACEBOOK -> Color(0xFF1877F2)
                MonitoredApp.X -> Color(0xFF1A1A1A)
                null -> Color(0xFF333333)
            }
            val initial = monitoredApp?.displayName?.take(1) ?: packageName.takeLast(1).uppercase()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brandColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    style = CookedTypography.monoCaption.copy(
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (monitoredApp == MonitoredApp.SNAPCHAT) Color.Black else Color.White
                    )
                )
            }
        }
    }
}

private fun loadAppIconBitmap(pm: PackageManager, packageName: String): ImageBitmap? {
    return try {
        val appInfo = pm.getApplicationInfo(packageName, 0)
        val drawable: Drawable = appInfo.loadIcon(pm)
        drawableToBitmap(drawable)?.asImageBitmap()
    } catch (_: Exception) {
        null
    }
}

private fun drawableToBitmap(drawable: Drawable): Bitmap? {
    return try {
        drawable.toBitmap(width = 72, height = 72, config = Bitmap.Config.ARGB_8888)
    } catch (_: Exception) {
        try {
            val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 72
            val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 72
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

@Composable
private fun AlertModeIcon(
    modifier: Modifier = Modifier,
    color: Color = CookedFlameOrange
) {
    Canvas(modifier = modifier.size(16.dp)) {
        val w = size.width
        val h = size.height

        // Clapper / bottom dot
        drawCircle(
            color = color,
            radius = w * 0.12f,
            center = Offset(w * 0.5f, h * 0.88f)
        )

        // Bell body
        val path = Path().apply {
            moveTo(w * 0.5f, h * 0.12f)
            cubicTo(w * 0.3f, h * 0.12f, w * 0.25f, h * 0.4f, w * 0.2f, h * 0.65f)
            lineTo(w * 0.1f, h * 0.75f)
            lineTo(w * 0.9f, h * 0.75f)
            lineTo(w * 0.8f, h * 0.65f)
            cubicTo(w * 0.75f, h * 0.4f, w * 0.7f, h * 0.12f, w * 0.5f, h * 0.12f)
            close()
        }
        drawPath(path = path, color = color)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C0E12)
@Composable
fun OnboardingCompleteScreenPreview() {
    CookedTheme {
        OnboardingCompleteContent(
            state = OnboardingSummaryUiState(
                monitoredAppCount = 3,
                monitoredAppPackages = listOf(
                    MonitoredApp.INSTAGRAM.packageName,
                    MonitoredApp.YOUTUBE.packageName,
                    MonitoredApp.REDDIT.packageName
                ),
                dailyLimit = 60,
                countStyleTitle = "Notification only",
                isLoading = false
            ),
            onStartClicked = {}
        )
    }
}
