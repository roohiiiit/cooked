package com.cooked.app.ui.components

import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.core.Fit
import app.rive.runtime.kotlin.core.Alignment as RiveAlignment
import com.cooked.app.R

@Composable
fun RiveMascot(
    modifier: Modifier = Modifier,
    @RawRes resId: Int = R.raw.mascot,
    stateMachineName: String? = "MascotSM",
    artboardName: String? = "Mascot",
    animationName: String? = null,
    autoplay: Boolean = true
) {
    if (LocalInspectionMode.current) {
        Image(
            painter = painterResource(id = R.drawable.zonk_welcome),
            contentDescription = stringResource(id = R.string.zonk_content_description),
            contentScale = ContentScale.Fit,
            modifier = modifier
        )
    } else {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                RiveAnimationView(context).apply {
                    setRiveResource(
                        resId = resId,
                        artboardName = artboardName,
                        animationName = animationName,
                        stateMachineName = stateMachineName,
                        autoplay = autoplay,
                        fit = Fit.CONTAIN,
                        alignment = RiveAlignment.CENTER
                    )
                }
            },
            update = { view ->
                // Ensure view is playing if needed
            }
        )
    }
}
