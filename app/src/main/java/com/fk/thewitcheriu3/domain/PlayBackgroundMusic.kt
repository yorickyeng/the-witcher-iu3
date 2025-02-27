package com.fk.thewitcheriu3.domain

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.fk.thewitcheriu3.R

@Composable
fun PlayBackgroundMusic() {
    val context = LocalContext.current

    // Создаём MediaPlayer
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.kaer_morhen).apply {
            isLooping = true // Зацикливаем музыку
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