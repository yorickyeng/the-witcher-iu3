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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.fk.thewitcheriu3.data.AppDatabase
import com.fk.thewitcheriu3.data.AppDatabase.Companion.MIGRATION_1_2
import com.fk.thewitcheriu3.data.GameMapRepository
import com.fk.thewitcheriu3.data.GameMapRepositoryImpl
import com.fk.thewitcheriu3.domain.PlayBackgroundMusic
import com.fk.thewitcheriu3.domain.models.NavRoutes
import com.fk.thewitcheriu3.ui.screens.MainMenu
import com.fk.thewitcheriu3.ui.screens.SettingsScreen
import com.fk.thewitcheriu3.ui.screens.GameMapCreatorScreen
import com.fk.thewitcheriu3.ui.screens.GameMapScreen
import com.fk.thewitcheriu3.ui.screens.SaveLoadScreen
import com.fk.thewitcheriu3.ui.theme.TheWitcherIU3Theme
import com.fk.thewitcheriu3.ui.viewmodels.GameMapViewModel

val LocalGameSavesRepository = staticCompositionLocalOf<GameMapRepository> {
    error("GameMapRepository not provided!")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appDatabase by lazy {
            Room.databaseBuilder(
                context = applicationContext,
                klass = AppDatabase::class.java,
                name = "game-database"
            ).addMigrations(MIGRATION_1_2).build()
        }

        val gameRepository by lazy {
            GameMapRepositoryImpl(appDatabase.gameDao())
        }

        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalGameSavesRepository provides gameRepository) {
                TheWitcherIU3Theme {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        App()
                    }
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

    val repository: GameMapRepository = LocalGameSavesRepository.current
    val viewModel: GameMapViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GameMapViewModel(repository) as T
            }
        }
    )

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRoutes.MainMenu.route) {
        composable(NavRoutes.MainMenu.route) {
            MainMenu(navController = navController)
        }
        composable(NavRoutes.NewGame.route) {
            GameMapScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        composable(NavRoutes.MapCreator.route) { GameMapCreatorScreen(navController = navController) }
        composable(NavRoutes.Settings.route) {
            SettingsScreen(
                navController = navController,
                onChangeMusicClicked = { playPhonk = !playPhonk },
                onStopMusicClicked = { music.stop() })
        }
        composable(NavRoutes.SaveLoadMenu.route) {
            SaveLoadScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
    }

    BackHandler { navController.navigate(NavRoutes.MainMenu.route) }
}

@Preview(showSystemUi = true)
@Composable
fun GameScreenPreview() {
    GameMapScreen(
        navController = TODO()
    )
}