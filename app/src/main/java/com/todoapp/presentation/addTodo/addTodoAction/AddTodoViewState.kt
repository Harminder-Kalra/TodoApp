package com.todoapp.presentation.addTodo.addTodoAction

data class AddTodoViewState(
    val todoText: String = "",
    val isTodoAddedSuccessfully: Boolean? = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)