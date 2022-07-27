@file:OptIn(ExperimentalComposeUiApi::class)

package com.sats.spikes.lookaheadlayout.sharedelements

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LookaheadLayoutScope
import coil.compose.AsyncImage
import com.sats.spikes.lookaheadlayout.Character
import com.sats.spikes.lookaheadlayout.MockCharacters
import com.sats.spikes.lookaheadlayout.animateConstraintsAndPlacement

@Composable
fun CharactersApp() {
    val characters = MockCharacters
    var selectedCharacter: Character? by remember { mutableStateOf(null) }

    val characterImages = remember {
        movableContentWithReceiverOf<LookaheadLayoutScope, Character, Boolean> { character, isDetails ->
            val modifier = if (isDetails) {
                Modifier.fillMaxWidth()
            } else {
                Modifier
                    .aspectRatio(1f)
                    .clip(CircleShape)
            }

            AsyncImage(
                modifier = modifier.animateConstraintsAndPlacement(),
                model = character.imageUrl,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
            )
        }
    }

    SharedElementsHost(Modifier.fillMaxSize()) {
        if (selectedCharacter == null) {
            CharacterOverview(
                characters = characters,
                onCharacterClick = { selectedCharacter = it },
                characterImage = { characterImages(it, false) }
            )
        } else {
            Detail(
                character = selectedCharacter!!,
                onClick = { selectedCharacter = null },
                characterImage = { characterImages(selectedCharacter!!, true) }
            )
        }
    }
}