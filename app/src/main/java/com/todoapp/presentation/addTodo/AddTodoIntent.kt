package com.todoapp.presentation.addTodo

sealed interface AddTodoIntent {
    data class EnterTodoText(val text: String) : AddTodoIntent
    data object AddTodo : AddTodoIntent
}