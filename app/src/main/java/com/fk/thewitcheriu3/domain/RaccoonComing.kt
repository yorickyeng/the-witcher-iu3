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

    var showOverlay by remember { mutableStateOf(false) } // затемнение и сообщение
    var isVisible by remember { mutableStateOf(false) } // анимация

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f, // прозрачность от 0 до 1
        animationSpec = tween(durationMillis = 1000), // длительность анимации 1 секунда
        label = "alpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.5f, // масштаб
        animationSpec = tween(durationMillis = 1000),
        label = "scale"
    )

    // запуск анимации при появлении енота
    LaunchedEffect(showRaccoon) {
        if (showRaccoon) {
            isVisible = true
            showOverlay = true
            delay(2000)
            showOverlay = false
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val cellSize = maxWidth / 10
        val showText = remember { mutableStateOf(false) }

        val raccoonWidth = cellSize * 2
        val raccoonHeight = cellSize * 2
        val raccoonX = cellSize * 2
        val raccoonY = cellSize * 6

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
                    .absoluteOffset(raccoonX, raccoonY) // позиция
                    .size(raccoonWidth, raccoonHeight)
                    .graphicsLayer { // анимация
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
                    text = "Isn't this a magical raccoon-necromancer? He hasn't been seen in ages... ",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }
}