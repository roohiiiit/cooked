package com.cooked.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cooked.app.ui.theme.CookedFlameOrange

/**
 * Modern elevated segmented capsule progress bar (Duolingo-style).
 * Replaces generic thin linear progress lines.
 */
@Composable
fun CookedSegmentedProgressBar(
    currentStep: Int,
    totalSteps: Int = 9,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalSteps) { index ->
            val stepNumber = index + 1
            val isFilled = stepNumber <= currentStep

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isFilled) CookedFlameOrange else Color(0xFF1E2228)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isFilled) Color(0xFFFF7043) else Color(0xFF2B313C),
                        shape = CircleShape
                    )
            ) {
                if (isFilled) {
                    // 3D tactile highlight on active pill segments
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp)
                            .background(Color(0x44FFFFFF))
                    )
                }
            }
        }
    }
}
