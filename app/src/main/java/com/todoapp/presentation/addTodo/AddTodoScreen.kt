package com.todoapp.presentation.addTodo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun AddTodoScreen(navController: NavController, viewModel: AddTodoViewModel = hiltViewModel()) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = viewState.isTodoAddedSuccessfully) {
        if (viewState.isTodoAddedSuccessfully == true) {
            navController.popBackStack()
        }
    }
    LaunchedEffect(key1 = viewState.errorMessage) {
        if (viewState.todoText.equals("Error", ignoreCase = true)) {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                "errorMsg",
                viewState.errorMessage
            )
            navController.popBackStack()
        }
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewState.todoText,
                onValueChange = { viewModel.onIntent(AddTodoIntent.EnterTodoText(it)) },
                label = { Text("TODO Item") },
                placeholder = { Text("Enter your TODO") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
            )

            Button(
                onClick = {
                    viewModel.onIntent(AddTodoIntent.AddTodo)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = viewState.todoText.isNotBlank(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Add TODO")
            }
        }
        if (viewState.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }

}