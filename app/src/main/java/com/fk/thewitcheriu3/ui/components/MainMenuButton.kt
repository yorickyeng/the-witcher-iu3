package com.fk.thewitcheriu3.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(vertical = 10.dp),
        onClick = onClick
    ) {
        Text(
            text = text, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif
        )
    }
}