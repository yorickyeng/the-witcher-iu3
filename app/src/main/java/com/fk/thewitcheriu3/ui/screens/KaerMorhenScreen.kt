package com.fk.thewitcheriu3.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.SpotType
import com.fk.thewitcheriu3.domain.models.characters.units.witchers.BearSchoolWitcher
import com.fk.thewitcheriu3.domain.models.characters.units.witchers.CatSchoolWitcher
import com.fk.thewitcheriu3.domain.models.characters.units.witchers.GWENTWitcher
import com.fk.thewitcheriu3.domain.models.characters.units.witchers.WolfSchoolWitcher

@Composable
fun KaerMorhenScreen(
    onPlayGwent: () -> Unit,
    onBuy: (String) -> Unit,
    onShowSpots: (SpotType) -> Unit,
    onClose: () -> Unit
) {
    val witchers = listOf(
        CatSchoolWitcher(),
        WolfSchoolWitcher(),
        BearSchoolWitcher(),
        GWENTWitcher()
    )

    var showWitchers by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.kaer_morhen_interior),
            contentDescription = "kaer morhen interior",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Transparent.copy(alpha = 0.7f), shape = RoundedCornerShape(40.dp))
                .border(border = BorderStroke(3.dp, Color.Black), shape = RoundedCornerShape(40.dp))
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Kaer Morhen",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (showWitchers) {
                for (witcher in witchers) {
                    Button(
                        onClick = { onBuy(witcher.getType()) }, modifier = Modifier.padding(8.dp)
                    ) {
                        Text("${witcher.getType()} (${witcher.getPrice()} orens)")
                    }
                }

                Button(
                    onClick = { showWitchers = !showWitchers },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Back")
                }
            } else {
                Button(
                    onClick = onPlayGwent, modifier = Modifier.padding(8.dp)
                ) {
                    Text("Play GWENT!")
                }

                Button(
                    onClick = { onShowSpots(SpotType.TABLE) }, modifier = Modifier.padding(8.dp)
                ) {
                    Text("Grab something to eat")
                }

                Button(
                    onClick = { onShowSpots(SpotType.BED) }, modifier = Modifier.padding(8.dp)
                ) {
                    Text("Have some rest")
                }

                Button(
                    onClick = { showWitchers = !showWitchers },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Get some friends")
                }

                Button(
                    onClick = onClose, modifier = Modifier.padding(8.dp)
                ) {
                    Text("Back To The Road")
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun KaerMorhenPreview() {
    KaerMorhenScreen(
        onBuy = {}, onClose = {},
        onPlayGwent = {}, onShowSpots = {}
    )
}