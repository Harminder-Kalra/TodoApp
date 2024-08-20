package com.todoapp.presentation.addTodo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.data.local.TodoItem
import com.todoapp.domain.use_case.addTodo.AddTodoUseCases
import com.todoapp.presentation.addTodo.addTodoAction.AddTodoIntent
import com.todoapp.presentation.addTodo.addTodoAction.AddTodoViewState
import com.todoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val addTodoUseCases: AddTodoUseCases
) : ViewModel() {
    private val _viewState = MutableStateFlow(AddTodoViewState())
    val viewState: StateFlow<AddTodoViewState> = _viewState

    fun onIntent(intent: AddTodoIntent) {
        when (intent) {
            is AddTodoIntent.EnterTodoText ->
                _viewState.update { it.copy(todoText = intent.text) }

            is AddTodoIntent.AddTodo ->
                addTodoItem(_viewState.value.todoText)
        }
    }

    private fun addTodoItem(title: String) {
        addTodoUseCases(TodoItem(title = title)).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            isTodoAddedSuccessfully = true
                        )
                    }
                }

                is Resource.Error -> {
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }

                is Resource.Loading -> {
                    _viewState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}