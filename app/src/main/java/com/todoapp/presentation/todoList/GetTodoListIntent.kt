package com.todoapp.presentation.todoList

sealed interface GetTodoListIntent {
    data object GetTodoList : GetTodoListIntent
    data class SearchQueryChanged(val query: String) : GetTodoListIntent

}