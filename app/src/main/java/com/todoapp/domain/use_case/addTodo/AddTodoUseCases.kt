package com.todoapp.domain.use_case.addTodo

import com.todoapp.data.local.TodoItem
import com.todoapp.domain.repository.TodoRepository
import com.todoapp.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddTodoUseCases @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(todoItem: TodoItem): Flow<Resource<Unit>> = flow {
        if (todoItem.title.equals("Error", ignoreCase = true)) {
            emit(Resource.Error("Failed to add TODO"))
        } else {
            try {
                emit(Resource.Loading())
                delay(3000)
                repository.insertTodoItem(todoItem)
                emit(Resource.Success())
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
    }
}


