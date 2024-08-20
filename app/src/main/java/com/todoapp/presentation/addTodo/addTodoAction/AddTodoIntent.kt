package com.todoapp.presentation.addTodo.addTodoAction

sealed interface AddTodoIntent {
    data class EnterTodoText(val text: String) : AddTodoIntent
    data object AddTodo : AddTodoIntent
}