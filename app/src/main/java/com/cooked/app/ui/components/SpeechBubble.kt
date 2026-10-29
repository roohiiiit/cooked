package com.cooked.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooked.app.ui.theme.CookedBubbleBackground
import com.cooked.app.ui.theme.CookedBubbleBorder
import com.cooked.app.ui.theme.CookedPrimaryText
import com.cooked.app.ui.theme.CookedTypography
import com.cooked.app.ui.theme.CookedWordmark

/**
 * Creates a rounded rectangular balloon with a pointer tail pointing toward the mascot on the left.
 * If pointerVerticalOffset is null, the pointer tail is automatically vertically centered.
 */
class SpeechBubbleShape(
    val cornerRadius: Dp = 18.dp,
    val pointerWidth: Dp = 12.dp,
    val pointerHeight: Dp = 16.dp,
    val pointerVerticalOffset: Dp? = null
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            with(density) {
                val pw = pointerWidth.toPx()
                val ph = pointerHeight.toPx()
                val cr = cornerRadius.toPx()
                val po = pointerVerticalOffset?.toPx() ?: ((size.height - ph) / 2f).coerceIn(cr * 0.6f, (size.height - ph - cr * 0.6f).coerceAtLeast(0f))

                // Main rounded rect shifted right by pointerWidth
                val rect = RoundRect(
                    left = pw,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    cornerRadius = CornerRadius(cr, cr)
                )
                addRoundRect(rect)

                // Tail pointer pointing left toward Zonk
                val tailPath = Path().apply {
                    moveTo(pw, po)
                    lineTo(0f, po + (ph / 2f))
                    lineTo(pw, po + ph)
                    close()
                }
                addPath(tailPath)
            }
        }
        return Outline.Generic(path)
    }
}

fun speechBubbleShape(
    cornerRadius: Dp = 18.dp,
    pointerWidth: Dp = 12.dp,
    pointerHeight: Dp = 16.dp,
    pointerVerticalOffset: Dp? = null
): Shape = SpeechBubbleShape(cornerRadius, pointerWidth, pointerHeight, pointerVerticalOffset)

@Composable
fun ZonkSpeechBubble(
    text: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    highlightWord: String? = null
) {
    val bubbleShape = remember { SpeechBubbleShape() }

    Box(
        modifier = modifier
            .background(CookedBubbleBackground, shape = bubbleShape)
            .border(1.5.dp, CookedBubbleBorder, shape = bubbleShape)
            .padding(start = 24.dp, end = 18.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            if (title != null) {
                Text(
                    text = title,
                    style = CookedTypography.dialogueTitle.copy(
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp
                    ),
                    color = CookedWordmark
                )
            }
            Text(
                text = text,
                style = CookedTypography.dialogueText.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                ),
                color = CookedPrimaryText
            )
        }
    }
}

/**
 * Duolingo-style avatar + dialogue header:
 * Docks larger, expressive Zonk avatar on the left and the speech bubble directly pointing to him on the right.
 */
@Composable
fun ZonkAvatarHeader(
    avatarRes: Int,
    dialogueText: String,
    modifier: Modifier = Modifier,
    dialogueTitle: String? = null,
    avatarSize: Dp = 90.dp,
    contentDescription: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(avatarSize)
        )

        ZonkSpeechBubble(
            text = dialogueText,
            title = dialogueTitle,
            modifier = Modifier.weight(1f)
        )
    }
}
