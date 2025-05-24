package com.fk.thewitcheriu3.domain

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.fk.thewitcheriu3.R

@Composable
fun playBackgroundMusic(audiofile: Int): MediaPlayer {
    val context = LocalContext.current

    // Создаём MediaPlayer
    val mediaPlayer = remember {
        MediaPlayer.create(context, audiofile).apply {
            if (audiofile != R.raw.raccoon_appearance && audiofile != R.raw.bober_kurwa) {
                isLooping = true
            } else {
                setVolume(2.0f, 2.0f) // Максимальная громкость
            }
            start() // Начинаем воспроизведение
        }
    }

    // Управляем жизненным циклом MediaPlayer
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release() // Освобождаем ресурсы при завершении
        }
    }

    return mediaPlayer
}

//@Composable
//fun playBackgroundMusic(audiofile: Int): MediaPlayer {
//    val context = LocalContext.current
//
//    return remember(audiofile) {
//        MediaPlayer.create(context, audiofile).apply {
//            if (audiofile != R.raw.raccoon_appearance && audiofile != R.raw.bober_kurwa) {
//                isLooping = true
//            } else {
//                setVolume(2.0f, 2.0f)
//            }
//        }
//    }
//}