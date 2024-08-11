package com.todoapp.domain.use_case.getTodo

import com.todoapp.data.local.TodoItem
import com.todoapp.domain.repository.TodoRepository
import com.todoapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTodoListUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(): Flow<Resource<List<TodoItem>>> = flow {
        try {
            emit(Resource.Loading())
            repository.getTodos().collect {
                emit(Resource.Success(it))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        }
    }
}