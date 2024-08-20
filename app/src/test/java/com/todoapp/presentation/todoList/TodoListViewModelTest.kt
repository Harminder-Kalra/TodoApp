package com.todoapp.presentation.todoList

import app.cash.turbine.test
import com.todoapp.data.local.TodoItem
import com.todoapp.domain.use_case.getTodo.GetTodoListUseCase
import com.todoapp.presentation.todoList.todoListAction.GetTodoListIntent
import com.todoapp.presentation.todoList.viewmodel.TodoListViewModel
import com.todoapp.utils.Resource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {

    private lateinit var getTodoListUseCase: GetTodoListUseCase
    private lateinit var viewModel: TodoListViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        getTodoListUseCase = mockk(relaxed = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = TodoListViewModel(getTodoListUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `should update view state with todo list items on success`() = runTest {
        val todoItems = listOf(TodoItem(1, "Test"))
        every { getTodoListUseCase.invoke() } returns flow {
            emit(Resource.Success(todoItems))
        }
        viewModel.onIntent(GetTodoListIntent.GetTodoList)

        viewModel.viewState.test {
            val state = awaitItem()
            assert(state.todoItemList == todoItems)
        }
        verify { getTodoListUseCase.invoke() }
    }


    @Test
    fun `when GetTodoList intent is received, getTodoListUseCase is called and updates viewState on error`() =
        runTest {
            val errorMessage = "Error occurred"
            val errorFlow = flowOf(Resource.Error<List<TodoItem>>(errorMessage))
            every { getTodoListUseCase.invoke() } returns errorFlow

            viewModel.onIntent(GetTodoListIntent.GetTodoList)

            viewModel.viewState.test {
                val state = awaitItem()
                assert(state.errorMessage == errorMessage)
            }
            verify { getTodoListUseCase.invoke() }
        }

    @Test
    fun `when SearchQueryChanged intent is received, filteredTodoItems are updated`() = runTest {
        val todoItems = listOf(
            TodoItem(title = "Buy milk"),
            TodoItem(title = "Walk the dog"),
            TodoItem(title = "Read a book")
        )

        val query = "book"
        val successFlow = flowOf(Resource.Success(todoItems))
        every { getTodoListUseCase.invoke() } returns successFlow

        viewModel.onIntent(GetTodoListIntent.GetTodoList)
        viewModel.onIntent(GetTodoListIntent.SearchQueryChanged(query))

        viewModel.viewState.test {
            val state = awaitItem()
            assert(state.searchQuery == query)
            assert(state.filteredTodoItems.size == 1)
            assert(state.filteredTodoItems.first().title.contains(query, ignoreCase = true))
        }
    }
}