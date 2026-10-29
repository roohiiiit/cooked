package com.cooked.app.ui.onboarding

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooked.app.R
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
import com.cooked.app.widget.LargeCookedWidgetReceiver
import com.cooked.app.widget.SmallCookedWidgetReceiver
import com.cooked.app.widget.WidgetPinnedReceiver

enum class WidgetVariant {
    SMALL, LARGE
}

/**
 * Screen 4: Widget Preview Animation & Pinning
 */
@Composable
fun AddWidgetOnboardingScreen(
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedVariant by remember { mutableStateOf(WidgetVariant.SMALL) }

    val onPinWidgetClicked = {
        pinWidget(context, selectedVariant == WidgetVariant.LARGE)
        onContinue()
    }

    AddWidgetContent(
        selectedVariant = selectedVariant,
        onSelectVariant = { selectedVariant = it },
        onPinWidget = onPinWidgetClicked,
        onSkip = onSkip,
        modifier = modifier
    )
}

fun pinWidget(context: Context, isLargeWidget: Boolean) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val providerClass = if (isLargeWidget) {
        LargeCookedWidgetReceiver::class.java
    } else {
        SmallCookedWidgetReceiver::class.java
    }
    val provider = ComponentName(context, providerClass)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            val successIntent = Intent(context, WidgetPinnedReceiver::class.java)
            val successPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                successIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            appWidgetManager.requestPinAppWidget(provider, null, successPendingIntent)
        } else {
            Toast.makeText(
                context,
                "Launcher does not support in-app pinning. Please add the widget from your home screen menu.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@Composable
fun AddWidgetContent(
    selectedVariant: WidgetVariant,
    onSelectVariant: (WidgetVariant) -> Unit,
    onPinWidget: () -> Unit,
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
        // 1. Elevated Segmented Capsule Progress Bar (Step 4 of 9)
        CookedSegmentedProgressBar(currentStep = 4)

        // 2. Duolingo-style Zonk Avatar + Speech Bubble Dialogue (Prominent & Visible)
        ZonkAvatarHeader(
            avatarRes = R.drawable.zonk_3_widget,
            dialogueTitle = stringResource(id = R.string.screen4_headline),
            dialogueText = stringResource(id = R.string.screen4_subheadline),
            avatarSize = 92.dp,
            modifier = Modifier
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 10.dp, bottom = 12.dp)
        )

        // 3. Center Area: Homescreen Mockup with Sequential Bouncy Animation Loop
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            HomescreenAnimationPreview()
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Standardized Widget Variant Selector (Below Preview)
        WidgetVariantSelector(
            selectedVariant = selectedVariant,
            onSelectVariant = onSelectVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenSideMargin)
        )

        // 5. Bottom Actions: Pin Button & Skip Link
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(top = 16.dp, bottom = Dimens.ButtonBottomInsetPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CookedPrimaryButton(
                text = stringResource(id = R.string.screen4_button_add_widget),
                onClick = onPinWidget,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.screen4_button_skip),
                style = CookedTypography.dialogueText.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = CookedMutedGrey
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onSkip)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

/**
 * Standardized Segmented Selector for Small vs Large widgets.
 */
@Composable
private fun WidgetVariantSelector(
    selectedVariant: WidgetVariant,
    onSelectVariant: (WidgetVariant) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(CookedSurface)
            .border(BorderStroke(1.dp, CookedCardBorder), RoundedCornerShape(14.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        VariantTabItem(
            text = stringResource(id = R.string.screen4_tab_small),
            isSelected = selectedVariant == WidgetVariant.SMALL,
            onClick = { onSelectVariant(WidgetVariant.SMALL) },
            modifier = Modifier.weight(1f)
        )

        VariantTabItem(
            text = stringResource(id = R.string.screen4_tab_large),
            isSelected = selectedVariant == WidgetVariant.LARGE,
            onClick = { onSelectVariant(WidgetVariant.LARGE) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun VariantTabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color(0xFF26201D) else Color.Transparent
    val borderModifier = if (isSelected) {
        Modifier.border(BorderStroke(1.5.dp, CookedFlameOrange), RoundedCornerShape(10.dp))
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .then(borderModifier)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = CookedTypography.dialogueTitle.copy(
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) CookedFlameOrange else CookedWordmark
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "AddWidget - Small Selected", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun AddWidgetPreviewSmall() {
    CookedTheme {
        AddWidgetContent(
            selectedVariant = WidgetVariant.SMALL,
            onSelectVariant = {},
            onPinWidget = {},
            onSkip = {}
        )
    }
}
