package com.cooked.app.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooked.app.ui.theme.CookedAccentOrange
import com.cooked.app.ui.theme.CookedButtonDisabledFill
import com.cooked.app.ui.theme.CookedButtonDisabledText
import com.cooked.app.ui.theme.CookedButtonText
import com.cooked.app.ui.theme.Dimens
import com.cooked.app.ui.theme.SpaceGroteskFontFamily

@Composable
fun CookedPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeight)
            .defaultMinSize(minHeight = Dimens.MinTouchTargetSize),
        enabled = enabled,
        shape = RoundedCornerShape(percent = 50),
        colors = ButtonDefaults.buttonColors(
            containerColor = CookedAccentOrange,
            contentColor = CookedButtonText,
            disabledContainerColor = CookedButtonDisabledFill,
            disabledContentColor = CookedButtonDisabledText
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontFamily = SpaceGroteskFontFamily,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) CookedButtonText else CookedButtonDisabledText
            )
        )
    }
}
