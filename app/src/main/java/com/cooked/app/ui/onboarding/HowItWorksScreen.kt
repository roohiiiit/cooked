package com.cooked.app.ui.onboarding

import android.provider.Settings
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooked.app.R
import com.cooked.app.ui.components.CookedPrimaryButton
import com.cooked.app.ui.components.CookedSegmentedProgressBar
import com.cooked.app.ui.components.ZonkSpeechBubble
import com.cooked.app.ui.theme.CookedBackground
import com.cooked.app.ui.theme.CookedMutedGrey
import com.cooked.app.ui.theme.CookedTheme
import com.cooked.app.ui.theme.CookedTypography
import com.cooked.app.ui.theme.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun HowItWorksScreen(
    onNext: () -> Unit = {},
    onSkip: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isAnimationScaleZero = remember(context) {
        try {
            val durationScale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f
            )
            val transitionScale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.TRANSITION_ANIMATION_SCALE,
                1f
            )
            durationScale == 0f || transitionScale == 0f
        } catch (_: Exception) {
            false
        }
    }

    var currentStep by remember { mutableIntStateOf(if (isAnimationScaleZero) 3 else 1) }
    var isTimelineFinished by remember { mutableStateOf(isAnimationScaleZero) }

    LaunchedEffect(isAnimationScaleZero) {
        if (isAnimationScaleZero) {
            currentStep = 3
            isTimelineFinished = true
            return@LaunchedEffect
        }

        while (isActive) {
            currentStep = 1
            delay(2000L)

            currentStep = 2
            delay(2000L)

            currentStep = 3
            isTimelineFinished = true
            delay(2000L)
        }
    }

    HowItWorksContent(
        step = currentStep,
        isTimelineFinished = isTimelineFinished,
        isForceFinalState = isAnimationScaleZero,
        onAnimationTap = { isTimelineFinished = true },
        onNext = onNext,
        onSkip = onSkip,
        modifier = modifier
    )
}

@Composable
fun HowItWorksContent(
    step: Int,
    isTimelineFinished: Boolean,
    isForceFinalState: Boolean = false,
    onAnimationTap: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit,
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
        // 1. Elevated Segmented Capsule Progress Bar (Step 2 of 9)
        CookedSegmentedProgressBar(currentStep = 2)

        // 2. Duolingo-style Zonk Avatar docked top-left + Speech Bubble on the right (Prominent & Visible)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 10.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Zonk Avatar synced with stage
            Box(
                modifier = Modifier.size(92.dp),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = step,
                    animationSpec = tween(durationMillis = 150),
                    label = "ZonkMascotCrossfade"
                ) { targetStep ->
                    val drawableRes = when (targetStep) {
                        1 -> R.drawable.zonk_1_happy
                        2 -> R.drawable.zonk_2_count
                        else -> R.drawable.zonk_3_widget
                    }
                    val contentDescRes = when (targetStep) {
                        1 -> R.string.screen2_zonk_happy_desc
                        2 -> R.string.screen2_zonk_count_desc
                        else -> R.string.screen2_zonk_widget_desc
                    }
                    Image(
                        painter = painterResource(id = drawableRes),
                        contentDescription = stringResource(id = contentDescRes),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Speech Bubble with crossfading dialogue
            Crossfade(
                targetState = step,
                animationSpec = tween(durationMillis = 150),
                modifier = Modifier.weight(1f),
                label = "SpeechLineFade"
            ) { targetStep ->
                val lineRes = when (targetStep) {
                    1 -> R.string.screen2_line_1
                    2 -> R.string.screen2_line_2
                    else -> R.string.screen2_line_3
                }
                ZonkSpeechBubble(
                    text = stringResource(id = lineRes),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 3. Center interactive animation area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onAnimationTap
                ),
            contentAlignment = Alignment.Center
        ) {
            ReelToWidgetExplainerAnimation(
                isForceFinalState = isForceFinalState
            )
        }

        // 4. Bottom Controls: Flame Orange pill "Next" + "Skip" link
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 8.dp, bottom = Dimens.ButtonBottomInsetPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CookedPrimaryButton(
                text = stringResource(id = R.string.button_next),
                onClick = onNext,
                enabled = isTimelineFinished,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            TextButton(
                onClick = onSkip,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .defaultMinSize(minHeight = Dimens.MinTouchTargetSize)
            ) {
                Text(
                    text = stringResource(id = R.string.button_skip),
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

@Preview(name = "HowItWorks - Step 1", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun HowItWorksPreview1() {
    CookedTheme {
        HowItWorksContent(
            step = 1,
            isTimelineFinished = false,
            onAnimationTap = {},
            onNext = {},
            onSkip = {}
        )
    }
}
