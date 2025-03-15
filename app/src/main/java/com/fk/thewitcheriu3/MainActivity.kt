package com.fk.thewitcheriu3

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.fk.thewitcheriu3.domain.PlayBackgroundMusic
import com.fk.thewitcheriu3.ui.NewGameScreen
import com.fk.thewitcheriu3.ui.game_map.GameMapScreen
import com.fk.thewitcheriu3.ui.theme.TheWitcherIU3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheWitcherIU3Theme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    var showNewGameScreen by rememberSaveable { mutableStateOf(true) }
    var playPhonk by rememberSaveable { mutableStateOf(false) }

    val music: MediaPlayer = if (playPhonk) {
        PlayBackgroundMusic(R.raw.slapper)
    } else {
        PlayBackgroundMusic(R.raw.kaer_morhen)
    }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        if (showNewGameScreen) {
            NewGameScreen(
                onNewGameClicked = { showNewGameScreen = !showNewGameScreen },
                onChangeMusicClicked = { playPhonk = !playPhonk },
                onStopMusicClicked = { music.stop() }
            )
        } else {
            GameMapScreen()
        }
    }

    BackHandler { showNewGameScreen = !showNewGameScreen }
}

@Preview(showSystemUi = true)
@Composable
fun GameScreenPreview() {
    GameMapScreen()
}