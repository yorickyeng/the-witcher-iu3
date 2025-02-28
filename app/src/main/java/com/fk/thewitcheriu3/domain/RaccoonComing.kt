package com.fk.thewitcheriu3.domain

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fk.thewitcheriu3.R

@Composable
fun RaccoonComing() {
    // Добавляем изображение енота
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
    ) {
        val cellSize = maxWidth / 10 // Размер одной клетки
        val showText = remember { mutableStateOf(false) }

        // Позиция и размер изображения енота
        val raccoonWidth = cellSize * 2 // Ширина = 2 клетки
        val raccoonHeight = cellSize * 2 // Высота = 2 клетки
        val raccoonX = cellSize * 2 // Начальная координата X = 2 клетки
        val raccoonY = cellSize * 6 // Начальная координата Y = 6 клеток

        Image(painter = painterResource(R.drawable.raccoon),
            contentDescription = "Raccoon Area",
            modifier = Modifier
                .absoluteOffset(raccoonX, raccoonY) // Позиция
                .size(raccoonWidth, raccoonHeight) // Размер
                .clickable {
                    showText.value = !showText.value
                })

        if (showText.value) {
            Text(
                text = "Isn't this a magical raccoon-necromancer? He hasn't been seen in ages... " + "\nNobody knows why does he come, he resurrects whoever he wants and vanishes.",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .padding(vertical = 20.dp),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
        }
    }
}