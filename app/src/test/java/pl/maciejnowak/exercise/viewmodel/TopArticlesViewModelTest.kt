package pl.maciejnowak.exercise.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.mockito.ArgumentCaptor.forClass
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import pl.maciejnowak.commonobjects.entities.TopArticle
import pl.maciejnowak.exercise.rule.MainCoroutineRule
import pl.maciejnowak.exercise.ui.viewmodel.TopArticlesViewModel
import pl.maciejnowak.repositories.ArticleRepository
import pl.maciejnowak.repositories.model.TopArticlesResult

@ExperimentalCoroutinesApi
@RunWith(Parameterized::class)
class TopArticlesViewModelTest(
    private val fetch: TopArticlesResult,
    private val result: TopArticlesResult,
    private val isSuccess: Boolean
) {

    @get:Rule val rule = InstantTaskExecutorRule()
    @get:Rule val coroutineRule = MainCoroutineRule()

    @Mock private lateinit var repository: ArticleRepository
    @Mock private lateinit var observerLoading: Observer<Boolean>

    private lateinit var viewModel: TopArticlesViewModel

    companion object {
        private val data: List<TopArticle> = emptyList()
        @Parameters @JvmStatic
        fun createData(): Collection<Array<Any>> {
            return listOf(
                arrayOf(TopArticlesResult.Success(data), TopArticlesResult.Success(data), true),
                arrayOf(TopArticlesResult.ErrorRefresh(data), TopArticlesResult.ErrorRefresh(data), true),
                arrayOf(TopArticlesResult.Error, TopArticlesResult.Error, false)
            )
        }
    }

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        runBlocking { `when`(repository.fetchTopArticles()).thenReturn(fetch) }
    }

    @After
    fun clean() {
        viewModel.isLoading.removeObserver(observerLoading)
    }

    @Test
    fun initViewModel_validateLiveData() {
        viewModel = TopArticlesViewModel(repository, coroutineRule.testDispatcherProvider)
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(result, viewModel.result.value)
        assertEquals(isSuccess, viewModel.isSuccess())
    }

    @Test
    fun loadTopArticles_validateLiveData() {
        viewModel = TopArticlesViewModel(repository, coroutineRule.testDispatcherProvider, false)
        viewModel.isLoading.observeForever(observerLoading)
        assertNull(viewModel.isLoading.value)
        assertNull(viewModel.result.value)
        viewModel.loadTopArticles()
        val captor = forClass(Boolean::class.java)
        verify(observerLoading, times(2)).onChanged(captor.capture())
        assertTrue(captor.allValues[0])
        assertFalse(captor.allValues[1])
        assertEquals(result, viewModel.result.value)
        assertEquals(isSuccess, viewModel.isSuccess())
    }
}