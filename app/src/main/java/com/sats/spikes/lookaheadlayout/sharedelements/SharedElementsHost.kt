package com.sats.spikes.lookaheadlayout.sharedelements

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LookaheadLayout
import androidx.compose.ui.layout.LookaheadLayoutScope

@ExperimentalComposeUiApi
@Composable
fun SharedElementsHost(modifier: Modifier = Modifier, content: @Composable LookaheadLayoutScope.() -> Unit) {
    LookaheadLayout(
        modifier = modifier,
        content = content,
        measurePolicy = { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }
            val maxWidth = placeables.maxOfOrNull { it.width } ?: 0
            val maxHeight = placeables.maxOfOrNull { it.height } ?: 0

            layout(maxWidth, maxHeight) {
                placeables.forEach {
                    it.place(0, 0)
                }
            }
        }
    )
}
