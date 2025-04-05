package com.fk.thewitcheriu3.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fk.thewitcheriu3.R
import com.fk.thewitcheriu3.domain.models.NavRoutes
import com.fk.thewitcheriu3.ui.components.MainMenuButton
import com.fk.thewitcheriu3.ui.viewmodels.GameMapViewModel

@Composable
fun SaveLoadScreen(
    navController: NavController,
    viewModel: GameMapViewModel = viewModel()
) {
    val saves by viewModel.getSavesList().collectAsState(emptyList())
    var textFieldValue by remember { mutableStateOf(TextFieldValue(viewModel.saveName)) }
    var showDeleteDialog by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.ciri_main_menu),
            contentDescription = "Ciri Main Menu",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Saves",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { 
                    textFieldValue = it
                    viewModel.saveName = it.text
                },
                label = { Text("Your name", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                MainMenuButton(
                    text = "Save",
                    onClick = { 
                        viewModel.saveGame(viewModel.saveName)
                        textFieldValue = TextFieldValue("")
                    }
                )

                MainMenuButton(
                    text = "Delete all saves",
                    onClick = { viewModel.deleteAllSaves() }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(saves) { (id, name) ->
                    SaveItem(
                        id = id,
                        name = name,
                        onLoadClick = {
                            viewModel.loadGame(id)
                            navController.navigate(NavRoutes.NewGame.route)
                        },
                        onDeleteClick = { showDeleteDialog = id }
                    )
                }
            }

            MainMenuButton(
                text = "To Main Menu",
                onClick = { navController.navigate(NavRoutes.MainMenu.route) }
            )
        }

        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Delete save?") },
                text = { Text("Are you sure you want to delete it?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog?.let { id ->
                                viewModel.deleteSave(id)
                                showDeleteDialog = null
                            }
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun SaveItem(
    id: Int,
    name: String,
    onLoadClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x80000000)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = name.ifEmpty { "NoName" },
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ID: $id",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onLoadClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Load")
                }

                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GameScreenPreview() {
    SaveLoadScreen(
        navController = TODO(),
        viewModel = TODO()
    )
}