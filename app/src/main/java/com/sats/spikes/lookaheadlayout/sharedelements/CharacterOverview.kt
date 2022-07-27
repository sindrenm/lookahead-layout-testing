package com.sats.spikes.lookaheadlayout.sharedelements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sats.spikes.lookaheadlayout.Character

@Composable
fun CharacterOverview(
    characters: List<Character>,
    onCharacterClick: (Character) -> Unit,
    characterImage: @Composable (character: Character) -> Unit,
) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        characters.forEach { character ->
            Box(
                Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .clickable { onCharacterClick(character) },
            ) {
                characterImage(character)
            }
        }
    }
}
