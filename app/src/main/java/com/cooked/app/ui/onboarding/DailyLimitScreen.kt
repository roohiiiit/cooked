package com.cooked.app.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.cooked.app.ui.theme.CookedBubbleBackground
import com.cooked.app.ui.theme.CookedBubbleBorder
import com.cooked.app.ui.theme.CookedCardBorder
import com.cooked.app.ui.theme.CookedFlameOrange
import com.cooked.app.ui.theme.CookedPrimaryText
import com.cooked.app.ui.theme.CookedSecondaryText
import com.cooked.app.ui.theme.CookedSurface
import com.cooked.app.ui.theme.CookedTheme
import com.cooked.app.ui.theme.CookedTypography
import com.cooked.app.ui.theme.Dimens
import com.cooked.app.ui.theme.JetBrainsMonoFontFamily
import kotlin.math.roundToInt

/**
 * Stateful Composable for Screen 8 ("Daily Limit Selector").
 */
@Composable
fun DailyLimitScreen(
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DailyLimitViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DailyLimitContent(
        state = uiState,
        onLimitChanged = viewModel::onLimitChanged,
        onLockInClicked = {
            viewModel.saveAndContinue(onNavigateNext)
        },
        modifier = modifier
    )
}

/**
 * Stateless Composable for Screen 8 ("Daily Limit Selector" - Duolingo-style redesign).
 */
@Composable
fun DailyLimitContent(
    state: DailyLimitUiState,
    onLimitChanged: (Int) -> Unit,
    onLockInClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CookedBackground)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Elevated Segmented Capsule Progress Bar (Step 8 of 9)
        CookedSegmentedProgressBar(currentStep = 8)

        // 2. Duolingo-style Zonk Avatar + Speech Bubble Dialogue (Prominent & Visible)
        ZonkAvatarHeader(
            avatarRes = R.drawable.zonk_limit,
            dialogueTitle = stringResource(id = R.string.screen8_headline),
            dialogueText = stringResource(id = R.string.screen8_subheadline),
            contentDescription = stringResource(id = R.string.zonk_limit_content_desc),
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
                // Live Counter Metric Display Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CookedSurface)
                        .border(1.dp, CookedCardBorder, RoundedCornerShape(20.dp))
                        .padding(vertical = 24.dp, horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Massive Mono Orange Number
                        Text(
                            text = state.currentLimit.toString(),
                            style = CookedTypography.counterDisplay
                        )

                        // Subtitle
                        Text(
                            text = stringResource(id = R.string.screen8_reels_per_day),
                            style = CookedTypography.dialogueText.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = CookedSecondaryText
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Dynamic Status Caption Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(CookedBubbleBackground)
                                .border(1.dp, CookedBubbleBorder, RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = DailyLimitViewModel.getStatusCaption(state.currentLimit),
                                style = CookedTypography.dialogueText.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CookedPrimaryText
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Custom Flame Orange Slider Component
                Slider(
                    value = state.currentLimit.toFloat(),
                    onValueChange = { newVal ->
                        val rounded = ((newVal / 5f).roundToInt() * 5).coerceIn(10, 250)
                        if (rounded != state.currentLimit) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onLimitChanged(rounded)
                        }
                    },
                    valueRange = 10f..250f,
                    colors = SliderDefaults.colors(
                        thumbColor = CookedFlameOrange,
                        activeTrackColor = CookedFlameOrange,
                        inactiveTrackColor = CookedCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Quick-Select Preset Chips (30, 60, 100, 150)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    state.presets.forEach { preset ->
                        val isSelected = preset == state.currentLimit
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) CookedFlameOrange else CookedSurface)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) CookedFlameOrange else CookedCardBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    if (!isSelected) {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        onLimitChanged(preset)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = preset.toString(),
                                style = CookedTypography.monoCaption.copy(
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color(0xFF000000) else CookedPrimaryText
                                )
                            )
                        }
                    }
                }
            }
        }

        // 4. Bottom Navigation CTA Button ("Lock in limit")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 12.dp, bottom = Dimens.ButtonBottomInsetPadding)
        ) {
            CookedPrimaryButton(
                text = stringResource(id = R.string.screen8_button_lock_in),
                onClick = onLockInClicked,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C0E12)
@Composable
fun DailyLimitScreenPreview() {
    CookedTheme {
        DailyLimitContent(
            state = DailyLimitUiState(currentLimit = 60),
            onLimitChanged = {},
            onLockInClicked = {}
        )
    }
}
