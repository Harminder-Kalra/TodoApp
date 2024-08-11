package com.todoapp.presentation.todoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todoapp.domain.use_case.getTodo.GetTodoListUseCase
import com.todoapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow(GetTodoViewState())
    val viewState: StateFlow<GetTodoViewState> = _viewState

    init {
        onIntent(GetTodoListIntent.GetTodoList)
    }

    fun onIntent(intent: GetTodoListIntent) {
        when (intent) {
            is GetTodoListIntent.GetTodoList ->
                getTodoListItem()

            is GetTodoListIntent.SearchQueryChanged ->
                filterTodoItems(intent.query)

        }
    }

    private fun getTodoListItem() {
        getTodoListUseCase.invoke().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            filteredTodoItems = result.data ?: emptyList(),
                            todoItemList = result.data ?: emptyList()
                        )
                    }
                }

                is Resource.Error -> {
                    _viewState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }

                is Resource.Loading -> {
                    _viewState.update { it.copy(isLoading = true) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun filterTodoItems(query: String) {
        _viewState.update {
            it.copy(
                searchQuery = query,
                filteredTodoItems = _viewState.value.todoItemList.filter { item ->
                    item.title.contains(
                        query,
                        ignoreCase = true
                    )
                }
            )
        }
    }
}