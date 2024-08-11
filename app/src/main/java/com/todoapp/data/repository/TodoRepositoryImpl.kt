package com.todoapp.data.repository

import com.todoapp.data.local.TodoDao
import com.todoapp.data.local.TodoItem
import com.todoapp.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoRepository {

    override suspend fun getTodos(): Flow<List<TodoItem>> = todoDao.getTodos()

    override suspend fun insertTodoItem(todoItem: TodoItem) =
        todoDao.insertTodoItem(todoItem)

}