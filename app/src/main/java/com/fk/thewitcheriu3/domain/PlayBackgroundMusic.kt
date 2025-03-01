package com.fk.thewitcheriu3.domain

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.fk.thewitcheriu3.R

@Composable
fun PlayBackgroundMusic(audiofile: Int) {
    val context = LocalContext.current

    // Создаём MediaPlayer
    val mediaPlayer = remember {
        MediaPlayer.create(context, audiofile).apply {
            if (audiofile == R.raw.kaer_morhen) {
                isLooping = true
            } // Зацикливаем музыку

            if (audiofile == R.raw.raccoon_appearance || audiofile == R.raw.bober_kurwa) {
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
}