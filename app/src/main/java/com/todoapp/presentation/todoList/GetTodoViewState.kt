package com.todoapp.presentation.todoList

import com.todoapp.data.local.TodoItem

data class GetTodoViewState(
    val todoItemList: List<TodoItem> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val filteredTodoItems: List<TodoItem> = emptyList(),
    val searchQuery: String = ""
)