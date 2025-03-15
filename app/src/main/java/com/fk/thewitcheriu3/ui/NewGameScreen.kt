package com.fk.thewitcheriu3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fk.thewitcheriu3.R

@Composable
fun NewGameScreen(
    onNewGameClicked: () -> Unit,
    onChangeMusicClicked: () -> Unit,
    onStopMusicClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.ciri_main_menu),
            contentDescription = "Ciri Main Menu",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.witcher_logo),
                contentDescription = "Witcher Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentScale = ContentScale.Crop
            )
            NewGameScreenButton("New Game", onNewGameClicked)
            NewGameScreenButton("Change Music", onChangeMusicClicked)
            NewGameScreenButton("Stop Music", onStopMusicClicked)
        }
    }
}

@Composable
fun NewGameScreenButton(text: String, onButtonClicked: () -> Unit) {
    Button(
        modifier = Modifier.padding(vertical = 10.dp),
        onClick = onButtonClicked
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
    }
}