package com.sats.spikes.lookaheadlayout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LookaheadLayout
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleScreen() {
    val character = MockCharacters[4]

    var isCentered by remember { mutableStateOf(true) }

    val widthModifier = if (isCentered) Modifier.width(256.dp) else Modifier.fillMaxWidth()

    LookaheadLayout(
        content = {
            Box(Modifier.fillMaxSize()) {
                val placementModifier = if (isCentered) Modifier.align(Alignment.Center) else Modifier

                Column(
                    widthModifier
                        .then(placementModifier)
                        .animateConstraints()
                        .animatePlacement()
                        .clickable(
                            onClick = { isCentered = !isCentered },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        )
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1920f / 1080f),
                        model = character.imageUrl,
                        contentDescription = character.name
                    )

                    Text(character.name, style = MaterialTheme.typography.h3)
                }
            }
        }
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val maxWidth = placeables.maxOf { it.width }
        val maxHeight = placeables.maxOf { it.height }

        layout(maxWidth, maxHeight) {
            placeables.forEach {
                it.place(0, 0)
            }
        }
    }
}