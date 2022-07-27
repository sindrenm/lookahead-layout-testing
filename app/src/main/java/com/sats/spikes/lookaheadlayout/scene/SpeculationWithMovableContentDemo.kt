@file:OptIn(ExperimentalComposeUiApi::class)

package com.sats.spikes.lookaheadlayout.scene

import androidx.compose.animation.demos.SceneHost
import androidx.compose.animation.demos.SceneScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun SpeculationWithMovableContentDemo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isSingleColumn by remember { mutableStateOf(true) }
        Column(
            Modifier
                .padding(100.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.clickable {
                isSingleColumn = true
            }, verticalAlignment = Alignment.CenterVertically) {
                RadioButton(isSingleColumn, { isSingleColumn = true })
                Text("Single Column")
            }
            Row(modifier = Modifier.clickable {
                isSingleColumn = false
            }, verticalAlignment = Alignment.CenterVertically) {
                RadioButton(!isSingleColumn, { isSingleColumn = false })
                Text("Double Column")
            }
        }
        val items = remember {
            colors.mapIndexed { id, color ->
                movableContentWithReceiverOf<SceneScope, Float> { weight ->
                    Box(
                        Modifier.padding(15.dp).height(80.dp)
                            .fillMaxWidth(weight)
                            .sharedElement()
                            .background(color, RoundedCornerShape(20)),
                        contentAlignment = Alignment.Center
                    ) {
                        when (id) {
                            0 -> CircularProgressIndicator(color = Color.White)
                            1 -> Box(Modifier.graphicsLayer {
                                scaleX = 0.5f
                                scaleY = 0.5f
                                translationX = 100f
                            }) {
                                AnimatedDots()
                            }
                            2 -> Box(Modifier.graphicsLayer {
                                scaleX = 0.5f
                                scaleY = 0.5f
                            }) { InfinitePulsingHeart() }
                            else -> InfiniteProgress()
                        }
                    }
                }
            }
        }
        SceneHost(Modifier.fillMaxSize()) {
            if (isSingleColumn) {
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    items.forEach {
                        it(0.8f)
                    }
                }
            } else {
                Row {
                    Column(Modifier.weight(1f)) {
                        items.forEachIndexed { id, item ->
                            if (id % 2 == 0) {
                                item(1f)
                            }
                        }
                    }
                    Column(Modifier.weight(1f)) {
                        items.forEachIndexed { id, item ->
                            if (id % 2 != 0) {
                                item(1f)
                            }
                        }
                    }
                }
            }
        }
    }
}

private val colors = listOf(
    Color(0xffff6f69),
    Color(0xffffcc5c),
    Color(0xff264653),
    Color(0xff2a9d84)
)