package com.fk.thewitcheriu3.ui.game_map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fk.thewitcheriu3.domain.entities.units.witchers.BearSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.entities.units.witchers.GWENTWitcher
import com.fk.thewitcheriu3.domain.entities.units.witchers.WolfSchoolWitcher

@Composable
fun BuyMenuScreen(
    onBuy: (String) -> Unit, // Функция для покупки юнита
    onClose: () -> Unit // Функция для закрытия меню
) {
    val witchers = listOf(
        CatSchoolWitcher(),
        WolfSchoolWitcher(),
        BearSchoolWitcher(),
        GWENTWitcher()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)) // Затемнение экрана
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.inverseSurface, shape = CircleShape)
                .border(border = BorderStroke(3.dp, Color.Black), shape = CircleShape)
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Buy Unit",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            for (witcher in witchers) {
                Button(
                    onClick = { onBuy(witcher.getType()) }, modifier = Modifier.padding(8.dp)
                ) {
                    Text("${witcher.getType()} (${witcher.getPrice()} orens)")
                }
            }

            Button(
                onClick = onClose, modifier = Modifier.padding(8.dp)
            ) {
                Text("Close")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BuyUnitMenuPreview() {
    BuyMenuScreen(onBuy = {}, onClose = {})
}