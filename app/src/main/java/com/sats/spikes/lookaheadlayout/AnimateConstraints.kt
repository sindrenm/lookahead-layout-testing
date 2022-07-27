package com.sats.spikes.lookaheadlayout

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.LookaheadLayoutScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
fun Modifier.animateConstraints(lookaheadLayoutScope: LookaheadLayoutScope): Modifier {
    return composed {
        var sizeAnimation: Animatable<IntSize, AnimationVector2D>? by remember {
            mutableStateOf(null)
        }

        var targetSize: IntSize? by remember { mutableStateOf(null) }

        LaunchedEffect(Unit) {
            snapshotFlow { targetSize }
                .filterNotNull()
                .filterNot { it == sizeAnimation?.value }
                .collect { size: IntSize ->
                    if (sizeAnimation != null) {
                        launch { sizeAnimation?.animateTo(size) }
                    } else {
                        sizeAnimation = Animatable(size, IntSize.VectorConverter)
                    }
                }
        }

        with(lookaheadLayoutScope) {
            intermediateLayout { measurable, _, lookaheadSize ->
                targetSize = lookaheadSize

                val actualSize = sizeAnimation?.value ?: lookaheadSize
                val constraints = Constraints.fixed(actualSize.width, actualSize.height)
                val placeable = measurable.measure(constraints)

                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            }
        }
    }
}
