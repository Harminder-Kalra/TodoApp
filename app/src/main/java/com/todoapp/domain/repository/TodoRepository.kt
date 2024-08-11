package com.todoapp.domain.repository

import com.todoapp.data.local.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun getTodos(): Flow<List<TodoItem>>
    suspend fun insertTodoItem(todoItem: TodoItem)
}