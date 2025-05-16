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

@Composable
fun TavernScreen(
    onPlayGwent: () -> Unit,
    onEat: () -> Unit,
    onClose: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.tavern_interior),
            contentDescription = "tavern interior",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Transparent.copy(alpha = 0.7f), shape = RoundedCornerShape(40.dp))
                .border(border = BorderStroke(3.dp, Color.Black), shape = RoundedCornerShape(40.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tavern",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = onPlayGwent, modifier = Modifier.padding(8.dp)
            ) {
                Text("Play GWENT!")
            }

            Button(
                onClick = onEat, modifier = Modifier.padding(8.dp)
            ) {
                Text("Grab something to eat")
            }

            Button(
                onClick = onPlayGwent, modifier = Modifier.padding(8.dp)
            ) {
                Text("Have some rest")
            }

            Button(
                onClick = onClose, modifier = Modifier.padding(8.dp)
            ) {
                Text("To Forest")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TavernPreview() {
    TavernScreen(
        onClose = {},
        onPlayGwent = {},
        onEat = {},
    )
}