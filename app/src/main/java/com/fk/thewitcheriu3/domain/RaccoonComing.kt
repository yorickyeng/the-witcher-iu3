package com.fk.thewitcheriu3.domain

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fk.thewitcheriu3.R
import kotlinx.coroutines.delay

@Composable
fun RaccoonComing(showRaccoon: Boolean) {
    PlayBackgroundMusic(R.raw.raccoon_appearance)
    PlayBackgroundMusic(R.raw.bober_kurwa)

    // Состояние для управления затемнением и сообщением
    var showOverlay by remember { mutableStateOf(false) }

    // Состояние для управления анимацией
    var isVisible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f, // Прозрачность от 0 до 1
        animationSpec = tween(durationMillis = 1000),
        label = "alpha" // Длительность анимации 1 секунда
    )
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.5f, // Масштаб от 0.5 до 1
        animationSpec = tween(durationMillis = 1000),
        label = "scale" // Длительность анимации 1 секунда
    )

    // Запуск анимации при появлении енота
    LaunchedEffect(showRaccoon) {
        if (showRaccoon) {
            isVisible = true
            showOverlay = true // Показываем затемнение и сообщение
            delay(2000) // Затемнение и сообщение остаются на 2 секунды
            showOverlay = false // Убираем затемнение и сообщение
        }
    }

    // Добавляем изображение енота
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val cellSize = maxWidth / 10 // Размер одной клетки
        val showText = remember { mutableStateOf(false) }

        // Позиция и размер изображения енота
        val raccoonWidth = cellSize * 2 // Ширина = 2 клетки
        val raccoonHeight = cellSize * 2 // Высота = 2 клетки
        val raccoonX = cellSize * 2 // Начальная координата X = 2 клетки
        val raccoonY = cellSize * 6 // Начальная координата Y = 6 клеток

        // Затемнение экрана и сообщение
        if (showOverlay) {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.75f))
                    .fillMaxSize()
                    .graphicsLayer {
                        this.alpha = alpha
                        this.scaleX = scale
                        this.scaleY = scale
                    }) {
                Text(
                    text = "The raccoon-necromancer has appeared!",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }
        }

        Box(
            Modifier.padding(vertical = 24.dp)
        ) {
            Image(painter = painterResource(R.drawable.raccoon),
                contentDescription = "Raccoon Area",
                modifier = Modifier
                    .absoluteOffset(raccoonX, raccoonY) // Позиция
                    .size(raccoonWidth, raccoonHeight) // Размер
                    .graphicsLayer {
                        this.alpha = alpha
                        this.scaleX = scale
                        this.scaleY = scale
                    }
                    .clickable {
                        showText.value = !showText.value
                    })
        }

        if (showText.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f / 3f)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Isn't this a magical raccoon-necromancer? He hasn't been seen in ages... " + "\nNobody knows why does he come, he resurrects whoever he wants and vanishes.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }
}