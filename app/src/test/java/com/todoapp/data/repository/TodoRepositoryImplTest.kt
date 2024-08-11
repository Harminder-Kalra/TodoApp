package com.todoapp.data.repository

import com.todoapp.data.local.TodoDao
import com.todoapp.data.local.TodoItem
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TodoRepositoryImplTest {

    private lateinit var todoDao: TodoDao
    private lateinit var todoRepository: TodoRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        todoDao = mockk()
        todoRepository = TodoRepositoryImpl(todoDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getTodos should return list of TodoItems`() = runTest {
        val todoList = listOf(
            TodoItem(id = 1, title = "Test Todo 1"),
            TodoItem(id = 2, title = "Test Todo 2")
        )
        coEvery { todoDao.getTodos() } returns flowOf(todoList)

        val result = todoRepository.getTodos()

        result.collect { list ->
            assert(list == todoList)
        }
        coVerify { todoDao.getTodos() }
    }

    @Test
    fun `insertTodoItem should call dao's insertTodoItem`() = runTest {
        val todoItem = TodoItem(id = 3, title = "New Todo")

        coEvery { todoDao.insertTodoItem(any()) } just Runs

        todoRepository.insertTodoItem(todoItem)

        coVerify { todoDao.insertTodoItem(todoItem) }
    }
}