package com.cooked.app.ui.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooked.app.ui.theme.CookedAccentOrange
import com.cooked.app.ui.theme.CookedSurface
import com.cooked.app.ui.theme.CookedTheme
import com.cooked.app.ui.theme.CookedWordmark
import kotlinx.coroutines.isActive
import kotlin.math.roundToInt

/**
 * Redesigned, self-contained 3-stage explainer animation for Screen 2:
 * 1. Stage 1 (0.0s – 2.8s): Short-video reel feed swiping up with +1 bouncy counter pop.
 * 2. Stage 2 (2.8s – 4.0s): Pull-back morph where the counter glides down into the widget.
 * 3. Stage 3 (4.0s – 5.5s): Widget snaps and locks in on home screen with real-time sync glow.
 * 4. Reset (5.5s – 6.0s): Smooth loop reset.
 */
@Composable
fun ReelToWidgetExplainerAnimation(
    modifier: Modifier = Modifier,
    isForceFinalState: Boolean = false
) {
    val animTime = remember { Animatable(0f) }

    LaunchedEffect(isForceFinalState) {
        if (isForceFinalState) {
            animTime.snapTo(4600f) // Pin in final widget hold state
            return@LaunchedEffect
        }
        while (isActive) {
            animTime.snapTo(0f)
            animTime.animateTo(
                targetValue = 6000f,
                animationSpec = tween(durationMillis = 6000, easing = LinearEasing)
            )
        }
    }

    val t = if (isForceFinalState) 4600f else animTime.value

    // ==========================================
    // Choreography Calculations
    // ==========================================

    // Phase 1: Reel scroll (600ms – 1300ms)
    val swipeProgress = when {
        t < 600f -> 0f
        t < 1300f -> FastOutSlowInEasing.transform((t - 600f) / 700f)
        else -> 1f
    }

    // Gesture trail (550ms – 1100ms)
    val gestureAlpha = when {
        t < 550f -> 0f
        t < 850f -> (t - 550f) / 300f
        t < 1150f -> 1f - ((t - 850f) / 300f)
        else -> 0f
    }
    val gestureY = when {
        t < 550f -> 80f
        t < 1150f -> 80f - ((t - 550f) / 600f) * 120f
        else -> -40f
    }

    // Number flip from 41 to 42 occurs at 1200ms
    val countText = if (t < 1200f) "41" else "42"

    // Counter pulse on snap (1200ms – 1700ms)
    val counterPulseScale = when {
        t < 1200f -> 1.0f
        t < 1350f -> 1.0f + 0.35f * ((t - 1200f) / 150f)
        t < 1700f -> 1.35f - 0.35f * ((t - 1350f) / 350f)
        else -> 1.0f
    }

    // +1 Popup (1200ms – 1800ms)
    val plusOneAlpha = when {
        t < 1200f -> 0f
        t < 1300f -> (t - 1200f) / 100f
        t < 1550f -> 1f
        t < 1850f -> 1f - ((t - 1550f) / 300f)
        else -> 0f
    }
    val plusOneYOffset = when {
        t < 1200f -> 0f
        t < 1850f -> -((t - 1200f) / 650f) * 26f
        else -> -26f
    }

    // Phase 2: Morph Transition (2800ms – 4000ms)
    val morphProgress = when {
        t < 2800f -> 0f
        t < 3900f -> FastOutSlowInEasing.transform((t - 2800f) / 1100f)
        else -> 1f
    }

    // Video Feed Alpha & Scale
    val feedAlpha = (1f - morphProgress * 1.2f).coerceIn(0f, 1f)
    val feedScale = 1f - (morphProgress * 0.12f)

    // Home Screen Alpha & Scale
    val homeAlpha = (morphProgress * 1.5f - 0.2f).coerceIn(0f, 1f)

    // Trajectory of the count number (42) from floating pill down into widget center
    // Floating pill center: Y = -92.dp. Widget center: Y = -10.dp.
    val counterYProgress = morphProgress
    val counterYOffsetDp = -92f + (counterYProgress * 84f)
    val counterFontScale = 1.0f + (morphProgress * 1.35f) // from ~16sp to ~38sp equivalent

    // Pill background fades out during morph
    val pillBgAlpha = (1f - morphProgress * 2.5f).coerceIn(0f, 1f)

    // Phase 3: Widget Container Assembly (3600ms – 5500ms)
    val widgetAssembleProgress = when {
        t < 3500f -> 0f
        t < 4200f -> FastOutSlowInEasing.transform((t - 3500f) / 700f)
        else -> 1f
    }
    val widgetAlpha = widgetAssembleProgress
    val widgetScale = when {
        t < 3500f -> 0.88f
        t < 4200f -> 0.88f + (0.15f * ((t - 3500f) / 700f))
        t < 4500f -> 1.03f - (0.03f * ((t - 4200f) / 300f))
        else -> 1.0f
    }

    // Sync glow pulse on widget border (4200ms – 5000ms)
    val glowProgress = when {
        t < 4200f -> 0f
        t < 4500f -> (t - 4200f) / 300f
        t < 5100f -> 1f - ((t - 4500f) / 600f)
        else -> 0f
    }

    // Reset fade (5500ms – 6000ms)
    val sceneAlpha = when {
        t < 5500f -> 1f
        t < 5950f -> 1f - ((t - 5500f) / 450f)
        else -> 0f
    }

    // ==========================================
    // UI Layout: Simulated Phone Device
    // ==========================================
    val phoneWidth = 224.dp
    val phoneHeight = 328.dp
    val phoneCornerRadius = 26.dp

    Box(
        modifier = modifier
            .size(width = phoneWidth, height = phoneHeight)
            .graphicsLayer { alpha = sceneAlpha },
        contentAlignment = Alignment.Center
    ) {
        // 1. Device Outer Shadow & Bezel Frame
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevation = 16.dp, shape = RoundedCornerShape(phoneCornerRadius), spotColor = Color(0x66000000))
                .border(
                    BorderStroke(1.5.dp, Color(0xFF2C2C2E)),
                    shape = RoundedCornerShape(phoneCornerRadius)
                )
                .clip(RoundedCornerShape(phoneCornerRadius))
                .background(Color(0xFF09090B))
        ) {
            // 2. LAYER A: Home Screen Desktop (Behind Feed, morphs in Phase 2 & 3)
            if (homeAlpha > 0f) {
                HomeScreenDesktopLayer(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = homeAlpha },
                    widgetAlpha = widgetAlpha,
                    widgetScale = widgetScale,
                    glowAlpha = glowProgress
                )
            }

            // 3. LAYER B: Reel Video Feed (Phase 1, swipes & fades during morph)
            if (feedAlpha > 0f) {
                ReelFeedLayer(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = feedAlpha
                            scaleX = feedScale
                            scaleY = feedScale
                        },
                    swipeProgress = swipeProgress
                )
            }

            // 4. LAYER C: Upward Swipe Touch Gesture Trail
            if (gestureAlpha > 0f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset { IntOffset(0, (gestureY * density).roundToInt()) }
                        .graphicsLayer { alpha = gestureAlpha }
                        .size(width = 36.dp, height = 48.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    CookedAccentOrange.copy(alpha = 0.6f),
                                    Color.Transparent
                                )
                            ),
                            RoundedCornerShape(percent = 50)
                        )
                )
            }

            // 5. LAYER D: Continuous Floating Counter (Glide trajectory into Widget)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset { IntOffset(0, (counterYOffsetDp * density).roundToInt()) }
                    .graphicsLayer {
                        scaleX = counterPulseScale
                        scaleY = counterPulseScale
                    },
                contentAlignment = Alignment.Center
            ) {
                // Floating Pill Background (Fades out during morph)
                if (pillBgAlpha > 0f) {
                    Box(
                        modifier = Modifier
                            .graphicsLayer { alpha = pillBgAlpha }
                            .clip(RoundedCornerShape(percent = 50))
                            .background(Color(0xEE141416))
                            .border(BorderStroke(1.dp, Color(0xFF333338)), RoundedCornerShape(percent = 50))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(CookedAccentOrange, CircleShape)
                            )
                            // Placeholder space for the number
                            Spacer(modifier = Modifier.width(22.dp))
                        }
                    }
                }

                // The continuous count number (41 -> 42 -> expands into widget metric)
                Text(
                    text = countText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookedWordmark
                    ),
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = counterFontScale
                            scaleY = counterFontScale
                        }
                        .offset(x = if (pillBgAlpha > 0.5f) 4.dp else 0.dp)
                )

                // Bouncy +1 Bubble
                if (plusOneAlpha > 0f) {
                    Text(
                        text = "+1",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CookedAccentOrange
                        ),
                        modifier = Modifier
                            .offset { IntOffset(34, (plusOneYOffset * density).roundToInt()) }
                            .graphicsLayer { alpha = plusOneAlpha }
                    )
                }
            }

            // 6. Top Notch & Status Bar (Minimal phone bezel elements)
            TopPhoneStatusBar()

            // 7. Bottom Home Navigation Bar Line
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 7.dp)
                    .size(width = 64.dp, height = 3.dp)
                    .background(Color(0xFF3E3E42), RoundedCornerShape(percent = 50))
            )
        }
    }
}

/**
 * Top Status Bar & Camera Cutout
 */
@Composable
private fun TopPhoneStatusBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 7.dp, start = 14.dp, end = 14.dp)
    ) {
        // Clock
        Text(
            text = "9:41",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF7A7A80)
            ),
            modifier = Modifier.align(Alignment.CenterStart)
        )

        // Center Camera Cutout
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 46.dp, height = 5.dp)
                .background(Color(0xFF1B1B1E), RoundedCornerShape(percent = 50))
        )

        // Minimal battery / wifi indicator
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 10.dp, height = 5.dp)
                    .border(BorderStroke(0.8.dp, Color(0xFF7A7A80)), RoundedCornerShape(1.dp))
                    .padding(0.8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.7f)
                        .background(Color(0xFF7A7A80))
                )
            }
        }
    }
}

/**
 * Layer 1: Reel Video Feed mockup with upward card swiping
 */
@Composable
private fun ReelFeedLayer(
    swipeProgress: Float,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val containerHeight = maxHeight.value

        // Card 1: Exits upward
        val card1Y = -swipeProgress * containerHeight * 1.05f
        // Card 2: Enters from bottom
        val card2Y = (1f - swipeProgress) * containerHeight * 1.05f

        // Card 1 (Current Reel)
        ReelCardContent(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF202128), Color(0xFF121217), Color(0xFF0E0E12))
            ),
            avatarColor = CookedAccentOrange,
            accentDotColor = Color(0xFFFF453A),
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, (card1Y * density).roundToInt()) }
        )

        // Card 2 (Incoming Reel)
        ReelCardContent(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF281E24), Color(0xFF161114), Color(0xFF0F0B0D))
            ),
            avatarColor = Color(0xFFFF9F0A),
            accentDotColor = CookedAccentOrange,
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, (card2Y * density).roundToInt()) }
        )
    }
}

/**
 * Abstract Vertical Short-Video UI Content (No real brand logos)
 */
@Composable
private fun ReelCardContent(
    brush: Brush,
    avatarColor: Color,
    accentDotColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush)
            .padding(horizontal = 12.dp, vertical = 20.dp)
    ) {
        // Abstract Right Action Column
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Heart action
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0x33FFFFFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(accentDotColor, CircleShape)
                )
            }

            // Comment action
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0x33FFFFFF), CircleShape)
            )

            // Share action
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0x33FFFFFF), CircleShape)
            )

            // Music Disc Spinning placeholder
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(BorderStroke(1.5.dp, Color(0xFF44444C)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(avatarColor, CircleShape)
                )
            }
        }

        // Abstract Bottom Metadata (Profile avatar, user handle bar, sound line)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 24.dp, start = 4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(avatarColor, CircleShape)
                )
                Box(
                    modifier = Modifier
                        .width(46.dp)
                        .height(5.dp)
                        .background(Color(0x66FFFFFF), RoundedCornerShape(percent = 50))
                )
            }

            Box(
                modifier = Modifier
                    .width(86.dp)
                    .height(4.dp)
                    .background(Color(0x44FFFFFF), RoundedCornerShape(percent = 50))
            )

            Box(
                modifier = Modifier
                    .width(58.dp)
                    .height(3.5.dp)
                    .background(Color(0x33FFFFFF), RoundedCornerShape(percent = 50))
            )
        }
    }
}

/**
 * Layer 2: Home Screen Desktop with Widget Snap
 */
@Composable
private fun HomeScreenDesktopLayer(
    widgetAlpha: Float,
    widgetScale: Float,
    glowAlpha: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF141418), Color(0xFF09090C))
                )
            )
            .padding(horizontal = 14.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // App Grid Background (Subtle placeholders)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Row of App Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(4) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFF1E1E24), RoundedCornerShape(6.dp))
                    )
                }
            }

            // Spacer for the center widget
            Spacer(modifier = Modifier.weight(1f))

            // Bottom App Dock
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .background(Color(0x22FFFFFF), RoundedCornerShape(14.dp))
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0xFF282830), CircleShape)
                        )
                    }
                }
            }
        }

        // Home Screen Widget Container (Forms around the landing count metric)
        if (widgetAlpha > 0f) {
            val borderColor = if (glowAlpha > 0.05f) {
                CookedAccentOrange.copy(alpha = glowAlpha.coerceIn(0.2f, 1f))
            } else {
                Color(0xFF262626)
            }

            Box(
                modifier = Modifier
                    .offset(y = (-10).dp)
                    .size(width = 178.dp, height = 108.dp)
                    .graphicsLayer {
                        alpha = widgetAlpha
                        scaleX = widgetScale
                        scaleY = widgetScale
                    }
                    .clip(RoundedCornerShape(16.dp))
                    .background(CookedSurface)
                    .border(BorderStroke(1.2.dp, borderColor), RoundedCornerShape(16.dp))
                    .padding(all = 12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Widget Header: Label
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TODAY'S SCROLLS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.8.sp,
                                color = Color(0xFF8E8E93)
                            )
                        )
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(CookedAccentOrange, CircleShape)
                        )
                    }

                    // Bottom info label
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Cooked",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = CookedAccentOrange
                            )
                        )
                        Box(
                            modifier = Modifier
                                .size(3.dp)
                                .background(CookedAccentOrange, CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ReelToWidgetExplainerAnimationPreview() {
    CookedTheme {
        Box(
            modifier = Modifier
                .size(360.dp, 400.dp)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            ReelToWidgetExplainerAnimation()
        }
    }
}
