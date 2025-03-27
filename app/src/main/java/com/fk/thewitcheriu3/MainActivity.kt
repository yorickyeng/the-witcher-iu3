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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fk.thewitcheriu3.domain.PlayBackgroundMusic
import com.fk.thewitcheriu3.domain.entities.NavRoutes
import com.fk.thewitcheriu3.ui.MainMenu
import com.fk.thewitcheriu3.ui.SettingsScreen
import com.fk.thewitcheriu3.ui.game_map.GameMapCreatorScreen
import com.fk.thewitcheriu3.ui.game_map.GameMapScreen
import com.fk.thewitcheriu3.ui.theme.TheWitcherIU3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheWitcherIU3Theme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    var playPhonk by rememberSaveable { mutableStateOf(false) }

    val music: MediaPlayer = if (playPhonk) {
        PlayBackgroundMusic(R.raw.slapper)
    } else {
        PlayBackgroundMusic(R.raw.kaer_morhen)
    }

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRoutes.MainMenu.route) {
        composable(NavRoutes.MainMenu.route) {
            MainMenu(navController = navController)
        }
        composable(NavRoutes.NewGame.route) { GameMapScreen() }
        composable(NavRoutes.MapCreator.route) { GameMapCreatorScreen(navController = navController) }
        composable(NavRoutes.Settings.route) {
            SettingsScreen(navController = navController,
                onChangeMusicClicked = { playPhonk = !playPhonk },
                onStopMusicClicked = { music.stop() })
        }
    }

    BackHandler { navController.navigate(NavRoutes.MainMenu.route) }
}

@Preview(showSystemUi = true)
@Composable
fun GameScreenPreview() {
    GameMapScreen()
}