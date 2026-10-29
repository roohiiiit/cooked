package com.cooked.app.ui.onboarding

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cooked.app.R
import com.cooked.app.data.model.CountDisplayStyle
import com.cooked.app.ui.components.CookedPrimaryButton
import com.cooked.app.ui.components.CookedSegmentedProgressBar
import com.cooked.app.ui.components.ZonkAvatarHeader
import com.cooked.app.ui.theme.CookedBackground
import com.cooked.app.ui.theme.CookedCardBorder
import com.cooked.app.ui.theme.CookedFlameOrange
import com.cooked.app.ui.theme.CookedMutedGrey
import com.cooked.app.ui.theme.CookedSurface
import com.cooked.app.ui.theme.CookedTheme
import com.cooked.app.ui.theme.CookedTypography
import com.cooked.app.ui.theme.CookedWordmark
import com.cooked.app.ui.theme.Dimens

/**
 * Stateful wrapper for Screen 5 ("Count Display Style").
 */
@Composable
fun CountStyleScreen(
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CountStyleViewModel = viewModel()
) {
    val selectedStyle by viewModel.uiState.collectAsStateWithLifecycle()

    CountStyleContent(
        selectedStyle = selectedStyle,
        onOptionSelected = { viewModel.onOptionSelected(it) },
        onNext = { viewModel.saveAndContinue(onNext) },
        modifier = modifier
    )
}

/**
 * Stateless Composable for Screen 5 ("Count Display Style").
 */
@Composable
fun CountStyleContent(
    selectedStyle: CountDisplayStyle,
    onOptionSelected: (CountDisplayStyle) -> Unit,
    onNext: () -> Unit,
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
        // 1. Elevated Segmented Capsule Progress Bar (Step 5 of 9)
        CookedSegmentedProgressBar(currentStep = 5)

        // 2. Duolingo-style Zonk Avatar + Speech Bubble Dialogue (Prominent & Visible)
        ZonkAvatarHeader(
            avatarRes = R.drawable.zonk_thinking,
            dialogueTitle = stringResource(id = R.string.screen5_headline),
            dialogueText = "Choose how your reel doomscroll counts are surfaced.",
            contentDescription = stringResource(id = R.string.zonk_thinking_content_desc),
            avatarSize = 92.dp,
            modifier = Modifier
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 10.dp, bottom = 12.dp)
        )

        // 3. Selection Cards (Centered in remaining viewport)
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card 1: Notification only (Recommended)
                DisplayOptionCard(
                    title = stringResource(id = R.string.screen5_option_notification_only),
                    subtitle = null,
                    badgeText = stringResource(id = R.string.screen5_badge_recommended),
                    isSelected = selectedStyle == CountDisplayStyle.NOTIFICATION_ONLY,
                    onClick = { onOptionSelected(CountDisplayStyle.NOTIFICATION_ONLY) }
                )

                // Card 2: Notification + milestones
                DisplayOptionCard(
                    title = stringResource(id = R.string.screen5_option_notification_milestones),
                    subtitle = null,
                    badgeText = null,
                    isSelected = selectedStyle == CountDisplayStyle.NOTIFICATION_MILESTONES,
                    onClick = { onOptionSelected(CountDisplayStyle.NOTIFICATION_MILESTONES) }
                )

                // Card 3: Floating counter (Needs an extra permission)
                DisplayOptionCard(
                    title = stringResource(id = R.string.screen5_option_floating_counter),
                    subtitle = stringResource(id = R.string.screen5_option_floating_counter_subtitle),
                    badgeText = null,
                    isSelected = selectedStyle == CountDisplayStyle.FLOATING_COUNTER,
                    onClick = { onOptionSelected(CountDisplayStyle.FLOATING_COUNTER) }
                )

                // Card 4: Hidden, stats only
                DisplayOptionCard(
                    title = stringResource(id = R.string.screen5_option_hidden_stats),
                    subtitle = null,
                    badgeText = null,
                    isSelected = selectedStyle == CountDisplayStyle.HIDDEN_STATS_ONLY,
                    onClick = { onOptionSelected(CountDisplayStyle.HIDDEN_STATS_ONLY) }
                )
            }
        }

        // 4. Bottom Action: Full-width Next Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 16.dp, bottom = Dimens.ButtonBottomInsetPadding),
            contentAlignment = Alignment.Center
        ) {
            CookedPrimaryButton(
                text = stringResource(id = R.string.button_next),
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Standardized Radio Option Card with dark surface, soft border, and flame orange active border.
 */
@Composable
private fun DisplayOptionCard(
    title: String,
    subtitle: String?,
    badgeText: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) CookedFlameOrange else CookedCardBorder
    val borderWidth = if (isSelected) 1.5.dp else 1.dp
    val cardShape = RoundedCornerShape(14.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(CookedSurface)
            .border(BorderStroke(borderWidth, borderColor), cardShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = if (subtitle != null) 14.dp else 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = CookedTypography.dialogueTitle.copy(
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = CookedWordmark
                    )
                )

                if (badgeText != null) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF2C1914))
                            .border(1.dp, Color(0xFF4A2518), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = badgeText,
                            style = CookedTypography.monoCaption.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CookedFlameOrange
                            )
                        )
                    }
                }
            }

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    style = CookedTypography.caption.copy(
                        fontSize = 12.sp,
                        color = CookedMutedGrey
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Custom Radio Circle
        CustomRadioIndicator(isSelected = isSelected)
    }
}

/**
 * Custom 20.dp Radio Circle with 10.dp inner dot when selected
 */
@Composable
private fun CustomRadioIndicator(
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val outerBorderColor = if (isSelected) CookedFlameOrange else Color(0xFF3A4250)

    Box(
        modifier = modifier
            .size(20.dp)
            .clip(CircleShape)
            .border(BorderStroke(1.5.dp, outerBorderColor), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(CookedFlameOrange)
            )
        }
    }
}

@Preview(name = "Screen 5 - Notification Only Selected", showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewCountStyleScreenNotificationOnly() {
    CookedTheme {
        CountStyleContent(
            selectedStyle = CountDisplayStyle.NOTIFICATION_ONLY,
            onOptionSelected = {},
            onNext = {}
        )
    }
}
