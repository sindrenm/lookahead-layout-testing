@file:OptIn(ExperimentalComposeUiApi::class)

package com.sats.spikes.lookaheadlayout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadLayoutScope
import androidx.compose.ui.unit.dp
import com.sats.spikes.lookaheadlayout.sharedelements.SharedElementsHost

@Composable
fun Moving() {
    var selectedColor: Color? by remember { mutableStateOf(null) }

    val items = remember {
        movableContentWithReceiverOf<LookaheadLayoutScope, Color> { color ->
            Box(
                Modifier
                    .animateConstraintsAndPlacement()
                    .fillMaxSize()
                    .background(color),
            )
        }
    }

    SharedElementsHost {
        Column {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            ) {
                colors.forEach { color ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(CircleShape),
                    ) {
                        if (selectedColor != color) {
                            Box(
                                Modifier.clickable(
                                    onClick = { selectedColor = color },
                                    enabled = selectedColor != color,
                                ),
                            ) {
                                items(color)
                            }
                        }
                    }
                }
            }

            Divider()

            Column(Modifier.weight(1f)) {
                selectedColor?.let { color ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                            .size(300.dp, 100.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { selectedColor = null },
                    ) {
                        items(color)
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
