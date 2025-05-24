package com.fk.thewitcheriu3.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.NpcSpot
import com.fk.thewitcheriu3.domain.models.SpotType

@Composable
fun SpotsScreen(
    spots: List<NpcSpot>,
    spotType: SpotType,
    onSpotSelected: (NpcSpot) -> Unit,
    onClose: () -> Unit,
    getOccupationTime: (NpcSpot) -> String,
    inTavern: Boolean,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(
                if (inTavern) R.drawable.tavern_interior
                else R.drawable.kaer_morhen_interior
            ),
            contentDescription = "interior",
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
                text = if (spotType == SpotType.BED) "Available Beds" else "Available Tables",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(spots) { spot ->
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(
                                if (spot.isOccupied) Color.Red.copy(alpha = 0.7f)
                                else Color.Green.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = getOccupationTime(spot),
                                color = Color.White,
                                fontSize = 16.sp
                            )
                            if (spot.occupiedBy != null) {
                                Text(
                                    text = "Occupied by: ${spot.occupiedBy?.getName()}",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                        Button(
                            onClick = { onSpotSelected(spot) },
                            enabled = !spot.isOccupied,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(if (spot.isOccupied) "Occupied" else "Select")
                        }
                    }
                }
            }

            Button(
                onClick = onClose,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Back")
            }
        }
    }
} 