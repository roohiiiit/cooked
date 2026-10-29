package com.cooked.app.ui.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooked.app.ui.theme.CookedTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Self-contained mockup phone canvas depicting a stylized Android homescreen
 * where the Small (2x2) and Large (4x2) widgets drop in sequentially with a spring bounce animation.
 */
@Composable
fun HomescreenAnimationPreview(
    modifier: Modifier = Modifier
) {
    // Small Widget Animatable States
    val smallAlpha = remember { Animatable(0f) }
    val smallTranslationY = remember { Animatable(-40f) }
    val smallScale = remember { Animatable(1.15f) }

    // Large Widget Animatable States
    val largeAlpha = remember { Animatable(0f) }
    val largeTranslationY = remember { Animatable(-40f) }
    val largeScale = remember { Animatable(1.15f) }

    // Sequential Loop
    LaunchedEffect(Unit) {
        val bounceSpring = spring<Float>(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )

        while (isActive) {
            // Phase 0 (0ms): Both widgets invisible, shifted up, and slightly scaled up
            smallAlpha.snapTo(0f)
            smallTranslationY.snapTo(-40f)
            smallScale.snapTo(1.15f)

            largeAlpha.snapTo(0f)
            largeTranslationY.snapTo(-40f)
            largeScale.snapTo(1.15f)

            delay(120)

            // Phase 1: Small Widget drops in and settles with a bounce
            launch { smallAlpha.animateTo(1f, tween(260)) }
            launch { smallTranslationY.animateTo(0f, bounceSpring) }
            launch { smallScale.animateTo(1f, bounceSpring) }

            // Phase 2: Hold (~600ms) while small widget rests
            delay(750)

            // Phase 3: Large Widget drops in below the small widget
            launch { largeAlpha.animateTo(1f, tween(260)) }
            launch { largeTranslationY.animateTo(0f, bounceSpring) }
            launch { largeScale.animateTo(1f, bounceSpring) }

            // Phase 4: Rest (~2500ms) for user inspection
            delay(2500)

            // Smooth fade out before looping
            launch { smallAlpha.animateTo(0f, tween(380)) }
            launch { largeAlpha.animateTo(0f, tween(380)) }
            delay(420)
        }
    }

    // Phone Frame Container
    Box(
        modifier = modifier
            .width(238.dp)
            .height(340.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1A1A), Color(0xFF0D0D0D))
                )
            )
            .border(BorderStroke(1.dp, Color(0xFF2A2A2A)), RoundedCornerShape(28.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Minimal Status Bar
            MockupStatusBar()

            Spacer(modifier = Modifier.height(6.dp))

            // 2. Homescreen Desktop Grid with Widgets
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Top Row: Small Widget (2x2) alongside 4 generic homescreen icons (2x2)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(104.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Small Widget with drop-in animation
                    Box(
                        modifier = Modifier
                            .weight(1.15f)
                            .fillMaxHeight()
                            .graphicsLayer {
                                alpha = smallAlpha.value
                                translationY = smallTranslationY.value * density
                                scaleX = smallScale.value
                                scaleY = smallScale.value
                            }
                    ) {
                        SmallWidgetMockup(
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // 4 Generic grid app icons beside the 2x2 widget
                    Column(
                        modifier = Modifier
                            .weight(0.85f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            GenericAppIcon(Color(0xFF263238))
                            GenericAppIcon(Color(0xFF37474F))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            GenericAppIcon(Color(0xFF455A64))
                            GenericAppIcon(Color(0xFF2E3B43))
                        }
                    }
                }

                // Bottom Row: Large Widget (4x2) spanning full width
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp)
                        .graphicsLayer {
                            alpha = largeAlpha.value
                            translationY = largeTranslationY.value * density
                            scaleX = largeScale.value
                            scaleY = largeScale.value
                        }
                ) {
                    LargeWidgetMockup(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Bottom Dock Placeholder (4 generic circular app icons)
            MockupDock()
        }
    }
}

@Composable
private fun MockupStatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "12:00",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFC0C0C0)
            )
        )

        // Status indicators (wifi, signal, battery dots)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFAAAAAA))
            )
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFAAAAAA))
            )
            Box(
                modifier = Modifier
                    .width(12.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .border(BorderStroke(0.8.dp, Color(0xFFAAAAAA)), RoundedCornerShape(1.dp))
            )
        }
    }
}

@Composable
private fun MockupDock() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x22FFFFFF))
                .padding(vertical = 6.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GenericAppIcon(Color(0xFF2C3E50), size = 22)
            GenericAppIcon(Color(0xFF27AE60), size = 22)
            GenericAppIcon(Color(0xFF2980B9), size = 22)
            GenericAppIcon(Color(0xFF8E44AD), size = 22)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Gesture navigation pill indicator
        Box(
            modifier = Modifier
                .width(42.dp)
                .height(2.5.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFF666666))
        )
    }
}

@Composable
private fun GenericAppIcon(
    color: Color,
    size: Int = 24
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size * 0.3f).dp))
            .background(color)
            .border(
                BorderStroke(0.5.dp, Color(0x33FFFFFF)),
                RoundedCornerShape((size * 0.3f).dp)
            )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewHomescreenAnimation() {
    CookedTheme {
        Box(
            modifier = Modifier.padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            HomescreenAnimationPreview()
        }
    }
}
