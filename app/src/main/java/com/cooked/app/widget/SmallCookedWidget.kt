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
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.cooked.app.R

class SmallCookedWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SmallCookedWidget()
}

class SmallCookedWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            SmallCookedWidgetContent()
        }
    }
}

@Composable
private fun SmallCookedWidgetContent() {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(24.dp)
            .background(ColorProvider(Color(0xFF141414)))
            .padding(14.dp)
    ) {
        Column(
            modifier = GlanceModifier.fillMaxSize()
        ) {
            // Top row: Label & accent dot
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TODAY'S SCROLLS",
                    style = TextStyle(
                        color = ColorProvider(Color(0xFF8E8E93)),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = GlanceModifier.defaultWeight())
                Box(
                    modifier = GlanceModifier
                        .size(6.dp)
                        .cornerRadius(3.dp)
                        .background(ColorProvider(Color(0xFF262626)))
                ) {}
            }

            Spacer(modifier = GlanceModifier.height(4.dp))

            // Main Metric
            Text(
                text = "142",
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            // Cooked status tag
            Text(
                text = "Cooked ●",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFFF6B00)),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // Mascot in bottom-right corner
        Box(
            modifier = GlanceModifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                provider = ImageProvider(R.drawable.zonk_cooked_sticker),
                contentDescription = "Zonk cooked",
                modifier = GlanceModifier.size(54.dp)
            )
        }
    }
}
