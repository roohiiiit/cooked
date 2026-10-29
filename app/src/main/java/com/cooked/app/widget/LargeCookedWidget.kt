package com.cooked.app.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.cooked.app.R
import com.cooked.app.data.OnboardingAppsDataStore
import kotlinx.coroutines.flow.first

class LargeCookedWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LargeCookedWidget()
}

class LargeCookedWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val selectedPackages = try {
            OnboardingAppsDataStore(context).monitoredAppsFlow.first()
        } catch (_: Exception) {
            emptySet()
        }

        provideContent {
            LargeCookedWidgetContent(selectedPackages = selectedPackages)
        }
    }
}

private data class AppStatItem(
    val name: String,
    val count: Int,
    val unit: String,
    val maxCount: Int,
    val iconResId: Int
)

@Composable
private fun LargeCookedWidgetContent(selectedPackages: Set<String>) {
    val defaultStats = listOf(
        AppStatItem("Instagram", 68, "reels", 80, R.drawable.ic_widget_instagram),
        AppStatItem("YouTube Shorts", 42, "shorts", 80, R.drawable.ic_widget_youtube),
        AppStatItem("Reddit", 20, "scrolls", 80, R.drawable.ic_widget_reddit),
        AppStatItem("TikTok", 12, "vids", 80, R.drawable.ic_widget_tiktok)
    )

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(24.dp)
            .background(ColorProvider(Color(0xFF141414)))
            .padding(12.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Column (~35% width)
            Column(
                modifier = GlanceModifier
                    .defaultWeight()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Top: Zonk icon thumbnail + COOKED label
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = GlanceModifier
                            .size(28.dp)
                            .cornerRadius(6.dp)
                            .background(ColorProvider(Color(0xFF1F1F1F)))
                            .padding(2.dp)
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.zonk_cooked_sticker),
                            contentDescription = "Zonk cooked",
                            modifier = GlanceModifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = GlanceModifier.width(6.dp))

                    Text(
                        text = "COOKED",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFFF6B00)),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(6.dp))

                // Middle: 142 REELS
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "142",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = GlanceModifier.width(4.dp))
                    Text(
                        text = "REELS",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFFF6B00)),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(2.dp))

                // Bottom: TOTAL COOKED Today
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL COOKED",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF8E8E93)),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Spacer(modifier = GlanceModifier.width(6.dp))
                    Text(
                        text = "Today",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF666666)),
                            fontSize = 8.sp
                        )
                    )
                }
            }

            Spacer(modifier = GlanceModifier.width(8.dp))

            // Subtle 1px vertical divider
            Box(
                modifier = GlanceModifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(ColorProvider(Color(0xFF222222)))
            ) {}

            Spacer(modifier = GlanceModifier.width(10.dp))

            // Right Column (~65% width): App breakdown list
            Column(
                modifier = GlanceModifier
                    .defaultWeight()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (item in defaultStats) {
                    GlanceAppProgressRow(item)
                    Spacer(modifier = GlanceModifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun GlanceAppProgressRow(item: AppStatItem) {
    Column(
        modifier = GlanceModifier.fillMaxWidth()
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Icon
            Image(
                provider = ImageProvider(item.iconResId),
                contentDescription = item.name,
                modifier = GlanceModifier.size(14.dp)
            )

            Spacer(modifier = GlanceModifier.width(5.dp))

            // App Name
            Text(
                text = item.name,
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = GlanceModifier.defaultWeight())

            // Count + unit
            Text(
                text = "${item.count} ${item.unit}",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFC0C0C0)),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = GlanceModifier.height(2.dp))

        // Progress bar
        val fraction = (item.count.toFloat() / item.maxCount.toFloat()).coerceIn(0f, 1f)
        LinearProgressIndicator(
            progress = fraction,
            color = ColorProvider(Color(0xFFFF6B00)),
            backgroundColor = ColorProvider(Color(0xFF262626)),
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(3.dp)
                .cornerRadius(2.dp)
        )
    }
}
