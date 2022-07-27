package com.sats.spikes.lookaheadlayout

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.LookaheadLayoutScope
import androidx.compose.ui.unit.*
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

        dbg("placementOffset (initial) = $placementOffset")
        dbg("targetOffset (initial) = $targetOffset")

        LaunchedEffect(Unit) {
            launch {
                snapshotFlow { targetSize }
                    .filterNotNull()
                    .filterNot { it == sizeAnimation?.value }
                    .collect { size: IntSize ->
                        if (sizeAnimation != null) {
                            launch {
                                dbg("Animate size from ${sizeAnimation?.value} → $size")
                                sizeAnimation?.animateTo(size/*, tween(5000)*/)
                            }
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
                            launch {
                                dbg("Animate offset from ${offsetAnimation?.value} → $offset")
                                offsetAnimation?.animateTo(offset/*, tween(5000)*/)
                            }
                        } else {
                            offsetAnimation = Animatable(offset, IntOffset.VectorConverter)
                            dbg("Constructed offsetAnimation from offset $offset: ${offsetAnimation?.value}")
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

            dbg("placementOffset = $placementOffset")
            dbg("targetOffset = $targetOffset")
//        }.drawBehind {
//            drawRect(
//                color = Color.Black,
//                style = Stroke(2f),
//                topLeft = Offset.Zero,
//                size = targetSize!!.toSize()
//            )
        }.intermediateLayout { measurable, _, lookaheadSize ->
            targetSize = lookaheadSize

            val actualSize = sizeAnimation?.value ?: lookaheadSize
            val constraints = Constraints.fixed(actualSize.width, actualSize.height)

            val placeable = measurable.measure(constraints)

            layout(placeable.width, placeable.height) {
                val offsetValue = offsetAnimation?.value
                dbg("offsetValue = $offsetValue")

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

fun dbg(string: String) {
    Log.d("DBG", string)
}