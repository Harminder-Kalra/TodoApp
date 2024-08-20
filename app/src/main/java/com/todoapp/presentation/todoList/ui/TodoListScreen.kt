package com.todoapp.presentation.todoList.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.todoapp.presentation.todoList.todoListAction.GetTodoListIntent
import com.todoapp.presentation.todoList.viewmodel.TodoListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoListScreen(
    navController: NavController,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val screenResultState = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("errorMsg", "")?.collectAsState()

    LaunchedEffect(screenResultState?.value) {
        screenResultState?.value.let {
            if (it.isNullOrEmpty().not()) {
                it?.let {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(it)
                    }
                }
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<String>("errorMsg")
            }
        }
    }

    LaunchedEffect(viewState.errorMessage) {
        if (viewState.errorMessage.isNullOrEmpty().not()) {
            snackBarHostState.showSnackbar(viewState.errorMessage.orEmpty())
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("errorKey")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_todo_screen") }) {
                Icon(Icons.Default.Add, contentDescription = "Add TODO")
            }
        },
        content = {
            if (viewState.todoItemList.isEmpty()) {
                Text(
                    "Press the + button to add a TODO item",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(
                            Alignment.Center
                        )
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    stickyHeader {
                        TextField(
                            value = viewState.searchQuery,
                            onValueChange = {
                                viewModel.onIntent(
                                    GetTodoListIntent.SearchQueryChanged(
                                        it
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Search Icon")
                            },
                        )
                    }
                    items(viewState.filteredTodoItems) { item ->
                        Text(
                            text = item.title,
                            color = Color.Black,
                            style = MaterialTheme.typography.displaySmall,
                            modifier = Modifier.padding(all = 20.dp)
                        )
                    }
                }
            }
        }
    )
}