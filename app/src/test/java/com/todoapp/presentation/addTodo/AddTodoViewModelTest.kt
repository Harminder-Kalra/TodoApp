package com.todoapp.presentation.addTodo

import com.todoapp.data.local.TodoItem
import com.todoapp.domain.use_case.addTodo.AddTodoUseCases
import com.todoapp.utils.Resource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddTodoViewModelTest {

    private lateinit var addTodoUseCases: AddTodoUseCases
    private lateinit var viewModel: AddTodoViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        addTodoUseCases = mockk()
        Dispatchers.setMain(testDispatcher)
        viewModel = AddTodoViewModel(addTodoUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `when EnterTodoText intent is received, todoText in viewState is updated`() = runTest {
        val todoText = "New Todo"
        viewModel.onIntent(AddTodoIntent.EnterTodoText(todoText))

        assert(viewModel.viewState.first().todoText == todoText)
    }

    @Test
    fun `when AddTodo intent is received, addTodoUseCases is called and updates viewState on success`() =
        runTest {
            val todoText = "New Todo"
            val successFlow = flowOf(Resource.Success(Unit))
            every { addTodoUseCases.invoke(TodoItem(title = todoText)) } returns successFlow

            viewModel.onIntent(AddTodoIntent.EnterTodoText(todoText))
            viewModel.onIntent(AddTodoIntent.AddTodo)

            val state = withTimeoutOrNull(1000) { viewModel.viewState.first() }

            assert(state?.isTodoAddedSuccessfully == true)
            assert(state?.isLoading == false)
            verify { addTodoUseCases(TodoItem(title = todoText)) }
        }

    @Test
    fun `when AddTodo intent is received, addTodoUseCases is called and updates viewState on error`() =
        runTest {
            val todoText = "New Todo"
            val errorMessage = "Error occurred"
            val errorFlow = flowOf(Resource.Error<Unit>(errorMessage))
            every { addTodoUseCases.invoke(TodoItem(title = todoText)) } returns errorFlow

            viewModel.onIntent(AddTodoIntent.EnterTodoText(todoText))
            viewModel.onIntent(AddTodoIntent.AddTodo)

            val state = withTimeoutOrNull(1000) { viewModel.viewState.first() }
            assert(state?.errorMessage == errorMessage)
            assert(state?.isLoading == false)
            verify { addTodoUseCases(TodoItem(title = todoText)) }
        }
}
