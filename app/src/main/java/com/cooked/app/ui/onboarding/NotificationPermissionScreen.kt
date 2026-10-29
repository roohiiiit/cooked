package com.cooked.app.ui.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cooked.app.R
import com.cooked.app.ui.components.CookedPrimaryButton
import com.cooked.app.ui.components.CookedSegmentedProgressBar
import com.cooked.app.ui.components.ZonkAvatarHeader
import com.cooked.app.ui.theme.CookedBackground
import com.cooked.app.ui.theme.CookedCardBorder
import com.cooked.app.ui.theme.CookedFlameOrange
import com.cooked.app.ui.theme.CookedMutedGrey
import com.cooked.app.ui.theme.CookedSecondaryText
import com.cooked.app.ui.theme.CookedSurface
import com.cooked.app.ui.theme.CookedTheme
import com.cooked.app.ui.theme.CookedTypography
import com.cooked.app.ui.theme.CookedWordmark
import com.cooked.app.ui.theme.Dimens

/**
 * Stateful Composable for Screen 7 ("Notification Permission").
 */
@Composable
fun NotificationPermissionScreen(
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationPermissionViewModel = viewModel()
) {
    val shouldBypass by viewModel.shouldBypass.collectAsStateWithLifecycle()

    // Screen 7 is automatically skipped if the user picked HIDDEN_STATS_ONLY on Screen 5
    LaunchedEffect(shouldBypass) {
        if (shouldBypass) {
            onNavigateNext()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Continue regardless of grant or deny
        onNavigateNext()
    }

    if (!shouldBypass) {
        NotificationPermissionContent(
            onAllowClicked = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    onNavigateNext()
                }
            },
            onSkipClicked = onNavigateNext,
            modifier = modifier
        )
    }
}

/**
 * Stateless Composable for Screen 7 ("Notification Permission" - Duolingo-style redesign).
 */
@Composable
fun NotificationPermissionContent(
    onAllowClicked: () -> Unit,
    onSkipClicked: () -> Unit,
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
        // 1. Elevated Segmented Capsule Progress Bar (Step 7 of 9)
        CookedSegmentedProgressBar(currentStep = 7)

        // 2. Duolingo-style Zonk Avatar + Speech Bubble Dialogue (Prominent & Visible)
        ZonkAvatarHeader(
            avatarRes = R.drawable.zonk_notification,
            dialogueTitle = stringResource(id = R.string.screen7_headline),
            dialogueText = stringResource(id = R.string.screen7_subheadline),
            contentDescription = stringResource(id = R.string.zonk_notification_content_desc),
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
                verticalArrangement = Arrangement.Center
            ) {
                // High-Fidelity Notification Mockup Card
                NotificationMockCard()

                Spacer(modifier = Modifier.height(20.dp))

                // Subtitle note
                Text(
                    text = "Cooked? never spams. You’ll only see quiet reel counts or milestone alerts you asked for.",
                    style = CookedTypography.caption,
                    color = CookedMutedGrey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        // 4. Action Buttons (Sticky at bottom)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 12.dp, bottom = Dimens.ButtonBottomInsetPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CookedPrimaryButton(
                text = stringResource(id = R.string.screen7_button_allow),
                onClick = onAllowClicked,
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(
                onClick = onSkipClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.ButtonHeight)
                    .defaultMinSize(minHeight = Dimens.MinTouchTargetSize)
            ) {
                Text(
                    text = stringResource(id = R.string.screen7_button_not_now),
                    style = CookedTypography.dialogueText.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = CookedMutedGrey
                    )
                )
            }
        }
    }
}

/**
 * Realistic tactile preview card demonstrating how notifications will appear.
 */
@Composable
private fun NotificationMockCard(
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(18.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(CookedSurface)
            .border(1.dp, CookedCardBorder, cardShape)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // App header bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(CookedFlameOrange),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔥",
                    fontSize = 12.sp
                )
            }

            Text(
                text = "Cooked?",
                style = CookedTypography.caption.copy(
                    fontWeight = FontWeight.Bold,
                    color = CookedWordmark
                )
            )

            Text(
                text = "•",
                style = CookedTypography.caption,
                color = CookedMutedGrey
            )

            Text(
                text = "Just now",
                style = CookedTypography.caption,
                color = CookedMutedGrey
            )
        }

        // Notification content
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Doomscroll Tracker Active",
                    style = CookedTypography.dialogueTitle.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookedWordmark
                    )
                )

                Text(
                    text = "45 reels watched today. 15 remaining before daily limit.",
                    style = CookedTypography.dialogueText.copy(
                        fontSize = 14.sp,
                        color = CookedSecondaryText,
                        lineHeight = 20.sp
                    )
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            // Badge Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x22FF5722))
                    .border(1.dp, Color(0x66FF5722), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "45 / 60",
                    style = CookedTypography.monoCaption.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookedFlameOrange
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C0E12)
@Composable
fun NotificationPermissionPreview() {
    CookedTheme {
        NotificationPermissionContent(
            onAllowClicked = {},
            onSkipClicked = {}
        )
    }
}
