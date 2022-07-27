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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LookaheadLayoutScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
fun Modifier.animatePlacement(lookaheadLayoutScope: LookaheadLayoutScope): Modifier {
    return composed {
        var offsetAnimation: Animatable<IntOffset, AnimationVector2D>? by remember {
            mutableStateOf(null)
        }

        var placementOffset: IntOffset by remember { mutableStateOf(IntOffset.Zero) }
        var targetOffset: IntOffset? by remember { mutableStateOf(null) }

        LaunchedEffect(Unit) {
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

        with(lookaheadLayoutScope) {
            onPlaced { lookaheadScopeCoordinates, layoutCoordinates ->
                placementOffset = lookaheadScopeCoordinates
                    .localPositionOf(layoutCoordinates, Offset.Zero)
                    .round()

                targetOffset = lookaheadScopeCoordinates
                    .localLookaheadPositionOf(layoutCoordinates)
                    .round()
            }.intermediateLayout { measurable, constraints, _ ->
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
}
