package pl.maciejnowak.repositories

import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import pl.maciejnowak.commonobjects.entities.TopArticle
import pl.maciejnowak.database.dao.ArticleDao
import pl.maciejnowak.network.FandomService
import pl.maciejnowak.network.model.ExpandedArticleResultSet
import pl.maciejnowak.repositories.mapper.TopArticleMapper
import pl.maciejnowak.repositories.model.TopArticlesResult
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ArticleRepositoryTest {

    @Mock private lateinit var topArticle: TopArticle
    @Mock private lateinit var topArticles: List<TopArticle>
    @Mock private lateinit var fandomService: FandomService
    @Mock private lateinit var articleDao: ArticleDao
    @Mock private lateinit var mapper: TopArticleMapper
    @InjectMocks private lateinit var repository: ArticleRepository

    @Before
    fun init() {
        runBlocking {
            `when`(articleDao.update(any())).thenReturn(Unit)
            `when`(articleDao.loadTopArticles()).thenReturn(topArticles)
        }
    }

    @Test
    fun localDataEmpty_fetchRemoteSuccess_resultSuccess() = runBlocking {
        `when`(articleDao.hasTopArticles()).thenReturn(null)
        `when`(fandomService.getTopArticles(anyInt())).thenReturn(Response.success(mock(ExpandedArticleResultSet::class.java)))
        assertEquals(TopArticlesResult.Success(topArticles), repository.fetchTopArticles(false))
    }

    @Test
    fun localDataEmpty_fetchRemoteError_resultError() = runBlocking {
        `when`(articleDao.hasTopArticles()).thenReturn(null)
        `when`(fandomService.getTopArticles(anyInt())).thenReturn(Response.error(500, ResponseBody.create(null, "")))
        assertEquals(TopArticlesResult.Error, repository.fetchTopArticles(false))
    }

    @Test
    fun localDataExpired_fetchRemoteSuccess_resultSuccess() = runBlocking {
        `when`(articleDao.hasTopArticles()).thenReturn(topArticle)
        `when`(articleDao.getTimeCreation()).thenReturn(0L)
        `when`(fandomService.getTopArticles(anyInt())).thenReturn(Response.success(mock(ExpandedArticleResultSet::class.java)))
        assertEquals(TopArticlesResult.Success(topArticles), repository.fetchTopArticles(false))
    }

    @Test
    fun localDataExpired_fetchRemoteError_resultErrorRefresh() = runBlocking {
        `when`(articleDao.hasTopArticles()).thenReturn(topArticle)
        `when`(articleDao.getTimeCreation()).thenReturn(0L)
        `when`(fandomService.getTopArticles(anyInt())).thenReturn(Response.error(500, ResponseBody.create(null, "")))
        assertEquals(TopArticlesResult.ErrorRefresh(topArticles), repository.fetchTopArticles(false))
    }

    @Test
    fun localDataValid_forceRefresh_fetchRemoteSuccess_resultSuccess() = runBlocking {
        `when`(articleDao.hasTopArticles()).thenReturn(topArticle)
        `when`(fandomService.getTopArticles(anyInt())).thenReturn(Response.success(mock(ExpandedArticleResultSet::class.java)))
        assertEquals(TopArticlesResult.Success(topArticles), repository.fetchTopArticles(true))
    }

    @Test
    fun localDataValid_forceRefresh_fetchRemoteError_resultErrorRefresh() = runBlocking {
        `when`(articleDao.hasTopArticles()).thenReturn(topArticle)
        `when`(fandomService.getTopArticles(anyInt())).thenReturn(Response.error(500, ResponseBody.create(null, "")))
        assertEquals(TopArticlesResult.ErrorRefresh(topArticles), repository.fetchTopArticles(true))
    }

    @Test
    fun localDataValid_resultSuccess() = runBlocking {
        `when`(articleDao.hasTopArticles()).thenReturn(topArticle)
        `when`(articleDao.getTimeCreation()).thenReturn(System.currentTimeMillis())
        assertEquals(TopArticlesResult.Success(topArticles), repository.fetchTopArticles(false))
    }

    private fun <T> any(): T = Mockito.any<T>()
}