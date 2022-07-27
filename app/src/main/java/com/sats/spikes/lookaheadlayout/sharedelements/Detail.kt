package com.sats.spikes.lookaheadlayout.sharedelements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sats.spikes.lookaheadlayout.Character

@Composable
fun Detail(
    character: Character,
    onClick: () -> Unit,
    characterImage: @Composable () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .align(Alignment.TopStart)
                .wrapContentHeight()
                .clickable(onClick = onClick),
        ) {
            Box(Modifier.aspectRatio(1920f / 1080f)) {
                characterImage()
            }

            Text(character.name, style = MaterialTheme.typography.h3)
        }
    }
}