package com.sats.spikes.lookaheadlayout

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LookaheadLayoutScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.round
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

context(LookaheadLayoutScope)
    @ExperimentalComposeUiApi
fun Modifier.animateConstraintsAndPlacement(): Modifier {
    return composed {
        var sizeAnimation: Animatable<IntSize, AnimationVector2D>? by remember {
            mutableStateOf(null)
        }

        var targetSize: IntSize? by remember { mutableStateOf(null) }

        var offsetAnimation: Animatable<IntOffset, AnimationVector2D>? by remember {
            mutableStateOf(null)
        }

        var placementOffset: IntOffset by remember { mutableStateOf(IntOffset.Zero) }
        var targetOffset: IntOffset? by remember { mutableStateOf(null) }

        LaunchedEffect(Unit) {
            launch {
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

            launch {
                snapshotFlow { targetOffset }
                    .filterNotNull()
                    .filterNot { it == offsetAnimation?.value }
                    .collect { offset: IntOffset ->
                        if (offsetAnimation != null) {
                            launch { offsetAnimation?.animateTo(offset) }
                        } else {
                            offsetAnimation = Animatable(offset, IntOffset.VectorConverter)
                        }
                    }
            }
        }

        onPlaced { lookaheadScopeCoordinates, layoutCoordinates ->
            placementOffset = lookaheadScopeCoordinates
                .localPositionOf(layoutCoordinates, Offset.Zero)
                .round()

            targetOffset = lookaheadScopeCoordinates
                .localLookaheadPositionOf(layoutCoordinates)
                .round()
        }.intermediateLayout { measurable, _, lookaheadSize ->
            targetSize = lookaheadSize

            val actualSize = sizeAnimation?.value ?: lookaheadSize
            val constraints = Constraints.fixed(actualSize.width, actualSize.height)

            val placeable = measurable.measure(constraints)

            layout(placeable.width, placeable.height) {
                val offsetValue = offsetAnimation?.value

                val (x, y) = if (offsetValue != null) {
                    offsetValue - placementOffset
                } else {
                    targetOffset!! - placementOffset
                }

                placeable.place(x, y)
            }
        }
    }
}
