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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooked.app.R
import com.cooked.app.ui.theme.CookedAccentOrange
import com.cooked.app.ui.theme.CookedMutedGrey
import com.cooked.app.ui.theme.CookedSurface
import com.cooked.app.ui.theme.CookedTheme
import com.cooked.app.ui.theme.CookedWordmark

/**
 * Small Widget Mockup (2x2):
 * Strictly matching design reference:
 * - Rounded rectangle (#141414 background, 1px border #262626)
 * - Top label: "TODAY'S SCROLLS" + indicator dot
 * - Large bold white count: "142"
 * - Status tag: "Cooked ●" in orange (#FF6B00)
 * - Zonk mascot sticker in the bottom-right corner
 */
@Composable
fun SmallWidgetMockup(
    modifier: Modifier = Modifier,
    count: String = "142"
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF141414))
            .border(BorderStroke(1.dp, Color(0xFF262626)), RoundedCornerShape(18.dp))
            .padding(11.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Top Row: Label & subtle dot
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.screen4_todays_scrolls),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = Color(0xFF8E8E93)
                        )
                    )

                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF2A2A2A))
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Main Metric: 142
                Text(
                    text = count,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        lineHeight = 30.sp
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Cooked Status Tag: "Cooked ●"
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cooked",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CookedAccentOrange
                        )
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(CookedAccentOrange)
                    )
                }
            }
        }

        // Zonk mascot sticker in the bottom-right corner
        Image(
            painter = painterResource(id = R.drawable.zonk_cooked_sticker),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 4.dp, y = 4.dp)
        )
    }
}

/**
 * Large Widget Mockup (4x2):
 * Strictly matching design reference:
 * - Rounded rectangle (#141414 background, 1px border #262626)
 * - Left column: Zonk badge + "COOKED", "142 REELS", "TOTAL COOKED Today"
 * - Subtle vertical divider
 * - Right column: 4 social app rows with orange progress bars
 */
@Composable
fun LargeWidgetMockup(
    modifier: Modifier = Modifier,
    totalCount: String = "142"
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF141414))
            .border(BorderStroke(1.dp, Color(0xFF262626)), RoundedCornerShape(18.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left column (~36% width)
        Column(
            modifier = Modifier
                .weight(0.95f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top: Zonk square badge + COOKED
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(17.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF1F1F1F))
                        .padding(1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.zonk_cooked_sticker),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(3.dp))

                Text(
                    text = "COOKED",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        color = CookedAccentOrange
                    ),
                    maxLines = 1
                )
            }

            // Middle: 142 REELS
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = totalCount,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        lineHeight = 19.sp
                    )
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "REELS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookedAccentOrange
                    ),
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }

            // Bottom: TOTAL COOKED Today
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TOTAL COOKED",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 6.2.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8E8E93)
                    ),
                    maxLines = 1,
                    softWrap = false
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 6.2.sp,
                        color = Color(0xFF555555)
                    ),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Subtle vertical divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(0.88f)
                .background(Color(0xFF222222))
        )

        Spacer(modifier = Modifier.width(6.dp))

        // Right column (64% width): 4 app breakdown rows
        Column(
            modifier = Modifier
                .weight(1.55f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            WidgetAppBreakdownRow(
                iconRes = R.drawable.ic_widget_instagram,
                name = "Instagram",
                count = "68",
                unit = "reels",
                fraction = 0.85f
            )
            WidgetAppBreakdownRow(
                iconRes = R.drawable.ic_widget_youtube,
                name = "YouTube Shorts",
                count = "42",
                unit = "shorts",
                fraction = 0.52f
            )
            WidgetAppBreakdownRow(
                iconRes = R.drawable.ic_widget_reddit,
                name = "Reddit",
                count = "20",
                unit = "scrolls",
                fraction = 0.25f
            )
            WidgetAppBreakdownRow(
                iconRes = R.drawable.ic_widget_tiktok,
                name = "TikTok",
                count = "12",
                unit = "vids",
                fraction = 0.15f
            )
        }
    }
}

@Composable
private fun WidgetAppBreakdownRow(
    iconRes: Int,
    name: String,
    count: String,
    unit: String,
    fraction: Float
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = name,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = count,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = unit,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 6.5.sp,
                        color = Color(0xFF8E8E93)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(1.5.dp))

        // Mini progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.5.dp)
                .clip(RoundedCornerShape(1.5.dp))
                .background(Color(0xFF262626))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .background(CookedAccentOrange)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewSmallWidgetMockup() {
    CookedTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SmallWidgetMockup(
                modifier = Modifier.size(width = 130.dp, height = 110.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewLargeWidgetMockup() {
    CookedTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LargeWidgetMockup(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
            )
        }
    }
}
