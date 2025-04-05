package com.fk.thewitcheriu3.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fk.thewitcheriu3.domain.models.NavRoutes
import com.fk.thewitcheriu3.ui.viewmodels.GameMapViewModel

@Composable
fun SaveLoadScreen(
    navController: NavController,
    viewModel: GameMapViewModel = viewModel()
) {
    val saves by viewModel.getSavesList().collectAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(saves) { (id, name) ->
                Row {
                    Text(name)
                    Button(onClick = {
                        viewModel.loadGame(id)
                        navController.navigate(NavRoutes.NewGame.route)
                        viewModel.loadGame(id)
                    }) {
                        Text("Load")
                    }
                }
            }
        }

        var saveName by remember { mutableStateOf("") }
        TextField(
            value = saveName,
            onValueChange = { saveName = it }
        )
        Button(
            onClick = {
                viewModel.saveGame(saveName)
            }
        ) {
            Text("Save")
        }
        Button(
            onClick = {
                viewModel.deleteAllSaves()
            }
        ) {
            Text("Delete All Saves")
        }
    }
}