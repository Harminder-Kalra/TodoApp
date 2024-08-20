package com.todoapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.todoapp.presentation.addTodo.ui.AddTodoScreen
import com.todoapp.presentation.navigation.Screen
import com.todoapp.presentation.todoList.ui.TodoListScreen
import com.todoapp.presentation.ui.theme.TodoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContent {
                TodoAppTheme {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Screen.TodoListScreen.route
                        ) {
                            composable(
                                route = Screen.TodoListScreen.route
                            ) {
                                TodoListScreen(navController)
                            }
                            composable(
                                route = Screen.AddTodoScreen.route
                            ) {
                                AddTodoScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoAppTheme {
        Greeting("Android")
    }
}