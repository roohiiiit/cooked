package com.cooked.app.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooked.app.R
import com.cooked.app.ui.components.CookedPrimaryButton
import com.cooked.app.ui.theme.CookedBackground
import com.cooked.app.ui.theme.CookedPrimaryText
import com.cooked.app.ui.theme.CookedSecondaryText
import com.cooked.app.ui.theme.CookedTheme
import com.cooked.app.ui.theme.CookedTypography
import com.cooked.app.ui.theme.CookedWordmark
import com.cooked.app.ui.theme.Dimens

@Composable
fun WelcomeScreen(
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
        // 1. Wordmark "Cooked?" at the top
        Text(
            text = stringResource(id = R.string.wordmark),
            style = CookedTypography.dialogueTitle.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = CookedWordmark
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = Dimens.WordmarkTopPadding)
        )

        Spacer(modifier = Modifier.weight(0.6f))

        // 2. Full-body Zonk mascot with a grounded drop shadow (appropriately scaled)
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.padding(horizontal = Dimens.MascotHorizontalPadding)
        ) {
            // Grounded drop shadow
            Canvas(
                modifier = Modifier
                    .width(180.dp)
                    .height(24.dp)
                    .offset(y = 10.dp)
            ) {
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x77000000),
                            Color(0x33000000),
                            Color.Transparent
                        )
                    ),
                    size = size
                )
            }

            // Mascot
            Image(
                painter = painterResource(id = R.drawable.zonk_welcome),
                contentDescription = stringResource(id = R.string.zonk_content_description),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 3. Headline & Subheadline
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = Dimens.ScreenSideMargin)
        ) {
            Text(
                text = stringResource(id = R.string.welcome_headline),
                style = CookedTypography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(id = R.string.welcome_body),
                style = CookedTypography.dialogueText.copy(
                    fontSize = 16.sp,
                    color = CookedSecondaryText,
                    lineHeight = 24.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 4. Primary CTA Button: "Let's go"
        CookedPrimaryButton(
            text = stringResource(id = R.string.welcome_button_lets_go),
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenSideMargin)
                .padding(bottom = Dimens.ButtonBottomInsetPadding)
        )
    }
}

@Preview(name = "Standard (360x800)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun WelcomeScreenPreviewStandard() {
    CookedTheme {
        WelcomeScreen(onContinue = {})
    }
}
