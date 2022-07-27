package com.sats.spikes.lookaheadlayout.scene

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun InfiniteProgress() {
    val infiniteTransition = rememberInfiniteTransition()

    Row {
        infiniteTransition.PulsingDot(StartOffset(0))
        infiniteTransition.PulsingDot(StartOffset(150, StartOffsetType.FastForward))
        infiniteTransition.PulsingDot(StartOffset(300, StartOffsetType.FastForward))
    }
}

@Composable
private fun InfiniteTransition.PulsingDot(startOffset: StartOffset) {
    val scale by animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = startOffset
        )
    )

    Box(
        Modifier
            .padding(5.dp)
            .size(20.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(Color.Gray, shape = CircleShape)
    )
}