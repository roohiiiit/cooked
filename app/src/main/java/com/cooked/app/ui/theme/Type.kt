package com.cooked.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.cooked.app.R

val GoogleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val SpaceGroteskFont = GoogleFont("Space Grotesk")
val SpaceGroteskFontFamily = FontFamily(
    Font(googleFont = SpaceGroteskFont, fontProvider = GoogleFontProvider, weight = FontWeight.Normal),
    Font(googleFont = SpaceGroteskFont, fontProvider = GoogleFontProvider, weight = FontWeight.Medium),
    Font(googleFont = SpaceGroteskFont, fontProvider = GoogleFontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = SpaceGroteskFont, fontProvider = GoogleFontProvider, weight = FontWeight.Bold)
)

val JetBrainsMonoFont = GoogleFont("JetBrains Mono")
val JetBrainsMonoFontFamily = FontFamily(
    Font(googleFont = JetBrainsMonoFont, fontProvider = GoogleFontProvider, weight = FontWeight.Normal),
    Font(googleFont = JetBrainsMonoFont, fontProvider = GoogleFontProvider, weight = FontWeight.Medium),
    Font(googleFont = JetBrainsMonoFont, fontProvider = GoogleFontProvider, weight = FontWeight.Bold)
)

val InterFont = GoogleFont("Inter")
val InterFontFamily = FontFamily(
    Font(googleFont = InterFont, fontProvider = GoogleFontProvider, weight = FontWeight.Normal),
    Font(googleFont = InterFont, fontProvider = GoogleFontProvider, weight = FontWeight.Medium),
    Font(googleFont = InterFont, fontProvider = GoogleFontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = InterFont, fontProvider = GoogleFontProvider, weight = FontWeight.Bold)
)

object CookedTypography {
    val dialogueTitle = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.2).sp,
        color = Color(0xFFFFFFFF)
    )

    val dialogueText = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp,
        color = Color(0xFFECEFF4)
    )

    val headlineLarge = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.5).sp,
        color = Color(0xFFFFFFFF)
    )

    val headlineMedium = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.3).sp,
        color = Color(0xFFFFFFFF)
    )

    val counterDisplay = TextStyle(
        fontFamily = JetBrainsMonoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 54.sp,
        lineHeight = 58.sp,
        letterSpacing = (-1.0).sp,
        color = CookedFlameOrange
    )

    val caption = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = Color(0xFF8E95A5)
    )

    val monoCaption = TextStyle(
        fontFamily = JetBrainsMonoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = Color(0xFF8E95A5)
    )

    val buttonText = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = CookedButtonText
    )
}

val Typography = Typography(
    headlineLarge = CookedTypography.headlineLarge,
    bodyLarge = TextStyle(
        fontFamily = SpaceGroteskFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = CookedSecondaryText
    ),
    titleLarge = CookedTypography.dialogueTitle,
    labelLarge = CookedTypography.buttonText
)
