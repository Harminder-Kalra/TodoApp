package com.todoapp.presentation.navigation

sealed class Screen(val route: String) {
    data object TodoListScreen: Screen("todo_list_screen")
    data object AddTodoScreen: Screen("add_todo_screen")
}