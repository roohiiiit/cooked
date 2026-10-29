package com.cooked.app.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.cooked.app.ui.theme.CookedSurface
import com.cooked.app.ui.theme.CookedTheme
import com.cooked.app.ui.theme.CookedTypography
import com.cooked.app.ui.theme.CookedWordmark
import com.cooked.app.ui.theme.Dimens

/**
 * Stateful wrapper for Screen 3 ("Pick Your Apps").
 */
@Composable
fun PickAppsScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PickAppsViewModel = viewModel()
) {
    val installedApps by viewModel.installedApps.collectAsStateWithLifecycle()
    val selectedApps by viewModel.selectedApps.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    PickAppsContent(
        installedApps = installedApps,
        selectedApps = selectedApps,
        isLoading = isLoading,
        onToggleApp = { packageName -> viewModel.toggleApp(packageName) },
        onContinue = { viewModel.onContinueClicked(onContinue) },
        modifier = modifier
    )
}

/**
 * Stateless Composable for Screen 3 ("Pick Your Apps").
 */
@Composable
fun PickAppsContent(
    installedApps: List<InstalledAppItem>,
    selectedApps: Set<String>,
    isLoading: Boolean,
    onToggleApp: (String) -> Unit,
    onContinue: () -> Unit,
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
        // 1. Elevated Segmented Capsule Progress Bar (Step 3 of 9)
        CookedSegmentedProgressBar(currentStep = 3)

        // 2. Duolingo-style Zonk Avatar + Speech Bubble Dialogue (Prominent & Visible)
        ZonkAvatarHeader(
            avatarRes = R.drawable.zonk_peeking,
            dialogueTitle = stringResource(id = R.string.screen3_headline),
            dialogueText = stringResource(id = R.string.screen3_subheadline),
            contentDescription = stringResource(id = R.string.zonk_nosy_content_desc),
            avatarSize = 92.dp,
            modifier = Modifier
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 10.dp, bottom = 12.dp)
        )

        // 3. Filtered App Selection List (Standardized Card Styling, Centered)
        if (installedApps.isEmpty() && !isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = Dimens.ScreenSideMargin),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.screen3_no_apps_found),
                    style = CookedTypography.dialogueText.copy(
                        fontSize = 15.sp,
                        color = CookedMutedGrey,
                        textAlign = TextAlign.Center
                    )
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = Dimens.ScreenSideMargin),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(installedApps, key = { it.app.packageName }) { item ->
                        val isSelected = item.app.packageName in selectedApps

                        AppSelectionRow(
                            item = item,
                            isSelected = isSelected,
                            onToggle = { onToggleApp(item.app.packageName) }
                        )
                    }
                }
            }
        }

        // 4. Bottom Action Button: Full-width pill "Continue"
        CookedPrimaryButton(
            text = stringResource(id = R.string.button_continue),
            onClick = onContinue,
            enabled = selectedApps.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 16.dp, bottom = Dimens.ButtonBottomInsetPadding)
        )
    }
}

/**
 * Standardized selectable row with dark surface, soft border, and flame orange active border.
 */
@Composable
fun AppSelectionRow(
    item: InstalledAppItem,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderStroke = if (isSelected) {
        BorderStroke(1.5.dp, CookedFlameOrange)
    } else {
        BorderStroke(1.dp, CookedCardBorder)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CookedSurface)
            .border(borderStroke, RoundedCornerShape(14.dp))
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Dynamic native app icon or fallback placeholder
            if (item.icon != null) {
                Image(
                    bitmap = item.icon,
                    contentDescription = item.app.displayName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0xFF2E3440), CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22262E))
                        .border(1.dp, Color(0xFF2E3440), CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // App Display Name
            Text(
                text = item.app.displayName,
                style = CookedTypography.dialogueTitle.copy(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CookedWordmark
                )
            )
        }

        // Toggle Switch reflecting selection state
        Switch(
            checked = isSelected,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CookedFlameOrange,
                uncheckedThumbColor = Color(0xFF8E95A5),
                uncheckedTrackColor = Color(0xFF22262E),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview(name = "PickApps - Standard", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PickAppsPreview() {
    CookedTheme {
        PickAppsContent(
            installedApps = listOf(
                InstalledAppItem(MonitoredApp.INSTAGRAM),
                InstalledAppItem(MonitoredApp.YOUTUBE),
                InstalledAppItem(MonitoredApp.REDDIT)
            ),
            selectedApps = setOf(MonitoredApp.INSTAGRAM.packageName),
            isLoading = false,
            onToggleApp = {},
            onContinue = {}
        )
    }
}
