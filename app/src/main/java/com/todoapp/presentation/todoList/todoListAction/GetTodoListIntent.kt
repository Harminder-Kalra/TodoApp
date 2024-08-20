package com.todoapp.presentation.todoList.todoListAction

sealed interface GetTodoListIntent {
    data object GetTodoList : GetTodoListIntent
    data class SearchQueryChanged(val query: String) : GetTodoListIntent

}