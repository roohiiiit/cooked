package com.cooked.app.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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
import com.cooked.app.ui.theme.CookedPrimaryText
import com.cooked.app.ui.theme.CookedSecondaryText
import com.cooked.app.ui.theme.CookedSurface
import com.cooked.app.ui.theme.CookedTheme
import com.cooked.app.ui.theme.CookedTypography
import com.cooked.app.ui.theme.CookedWordmark
import com.cooked.app.ui.theme.Dimens
import kotlinx.coroutines.flow.collectLatest

/**
 * Stateful Composable for Screen 6 ("Accessibility Permission & Prominent Disclosure").
 */
@Composable
fun AccessibilityDisclosureScreen(
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccessibilityDisclosureViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    val showNoThanksDialog by viewModel.showNoThanksDialog.collectAsStateWithLifecycle()

    // Listen to ON_RESUME to verify whether accessibility service was enabled in Settings
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onResumeCheck(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Collect one-shot navigation and snackbar events
    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is AccessibilityScreenEvent.NavigateNext -> {
                    onNavigateNext()
                }
                is AccessibilityScreenEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.messageResId),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    AccessibilityDisclosureContent(
        snackbarHostState = snackbarHostState,
        showNoThanksDialog = showNoThanksDialog,
        onAgreeClicked = { viewModel.onAgreeClicked(context) },
        onNoThanksClicked = { viewModel.onNoThanksClicked() },
        onDismissDialog = { viewModel.onDismissNoThanksDialog() },
        modifier = modifier
    )
}

/**
 * Stateless content Composable for Screen 6 (Duolingo-style redesign).
 */
@Composable
fun AccessibilityDisclosureContent(
    snackbarHostState: SnackbarHostState,
    showNoThanksDialog: Boolean,
    onAgreeClicked: () -> Unit,
    onNoThanksClicked: () -> Unit,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CookedBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Elevated Segmented Capsule Progress Bar (Step 6 of 9)
            CookedSegmentedProgressBar(currentStep = 6)

            // 2. Duolingo-style Zonk Avatar + Speech Bubble Dialogue (Prominent & Visible)
            ZonkAvatarHeader(
                avatarRes = R.drawable.zonk_shield,
                dialogueTitle = stringResource(id = R.string.screen6_headline),
                dialogueText = "I need accessibility access to count your scrolls in real time. Your privacy is 100% protected.",
                contentDescription = stringResource(id = R.string.zonk_shield_content_desc),
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
                    // Disclosure Cards (Standardized Dark Surfaces with Duolingo Accent Dots)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Card 1: What I read
                        DisclosureCard(
                            indicatorColor = CookedFlameOrange,
                            title = stringResource(id = R.string.screen6_card1_title),
                            body = stringResource(id = R.string.screen6_card1_body)
                        )

                        // Card 2: What I never collect
                        DisclosureCard(
                            indicatorColor = Color(0xFF4CAF50), // Reassuring green dot
                            title = stringResource(id = R.string.screen6_card2_title),
                            body = stringResource(id = R.string.screen6_card2_body)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Privacy Reassurance Line
                    Text(
                        text = stringResource(id = R.string.screen6_privacy_reassurance),
                        style = CookedTypography.caption,
                        color = CookedMutedGrey,
                        modifier = Modifier.padding(horizontal = 4.dp)
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
                    text = stringResource(id = R.string.screen6_button_agree),
                    onClick = onAgreeClicked,
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(
                    onClick = onNoThanksClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ButtonHeight)
                        .defaultMinSize(minHeight = Dimens.MinTouchTargetSize)
                ) {
                    Text(
                        text = stringResource(id = R.string.screen6_button_no_thanks),
                        style = CookedTypography.dialogueText.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = CookedMutedGrey
                        )
                    )
                }
            }
        }

        // 5. Non-intrusive Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 130.dp)
                .padding(horizontal = Dimens.ScreenSideMargin),
            snackbar = { snackbarData ->
                Snackbar(
                    containerColor = Color(0xFF1E2228),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        style = CookedTypography.dialogueText.copy(
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    )
                }
            }
        )

        // 6. Informative Dialog for "No thanks"
        if (showNoThanksDialog) {
            AlertDialog(
                onDismissRequest = onDismissDialog,
                containerColor = Color(0xFF16191E),
                titleContentColor = CookedWordmark,
                textContentColor = CookedSecondaryText,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text = stringResource(id = R.string.screen6_dialog_title),
                        style = CookedTypography.dialogueTitle.copy(
                            fontSize = 20.sp
                        )
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.screen6_dialog_body),
                        style = CookedTypography.dialogueText.copy(
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                            color = CookedSecondaryText
                        )
                    )
                },
                confirmButton = {
                    TextButton(onClick = onDismissDialog) {
                        Text(
                            text = stringResource(id = R.string.screen6_dialog_button),
                            style = CookedTypography.dialogueTitle.copy(
                                fontSize = 16.sp,
                                color = CookedFlameOrange
                            )
                        )
                    }
                }
            )
        }
    }
}

/**
 * Individual Policy Disclosure Card with Duolingo Accent Indicator
 */
@Composable
private fun DisclosureCard(
    indicatorColor: Color,
    title: String,
    body: String,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(CookedSurface)
            .border(BorderStroke(1.dp, CookedCardBorder), cardShape)
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(indicatorColor)
            )

            Text(
                text = title,
                style = CookedTypography.dialogueTitle.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = CookedWordmark
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = body,
            style = CookedTypography.dialogueText.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 21.sp,
                color = CookedSecondaryText
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0C0E12)
@Composable
fun AccessibilityDisclosurePreview() {
    CookedTheme {
        AccessibilityDisclosureContent(
            snackbarHostState = remember { SnackbarHostState() },
            showNoThanksDialog = false,
            onAgreeClicked = {},
            onNoThanksClicked = {},
            onDismissDialog = {}
        )
    }
}
