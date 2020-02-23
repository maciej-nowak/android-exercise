package pl.maciejnowak.repositories

import kotlinx.coroutines.runBlocking
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
import pl.maciejnowak.commonobjects.entities.TopWiki
import pl.maciejnowak.database.dao.WikiDao
import pl.maciejnowak.network.FandomService
import pl.maciejnowak.network.model.ExpandedWikiaResultSet
import pl.maciejnowak.repositories.mapper.TopWikiMapper
import pl.maciejnowak.repositories.model.TopWikisResult
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class WikiRepositoryTest {

    @Mock private lateinit var fandomService: FandomService
    @Mock private lateinit var wikiDao: WikiDao
    @Mock private lateinit var mapper: TopWikiMapper
    @InjectMocks private lateinit var repository: WikiRepository

    @Mock private lateinit var topWiki: TopWiki
    @Mock private lateinit var topWikis: List<TopWiki>
    @Mock private lateinit var responseSuccess: Response<ExpandedWikiaResultSet>
    @Mock private lateinit var responseError: Response<ExpandedWikiaResultSet>

    @Before
    fun init() {
        runBlocking {
            `when`(wikiDao.update(any())).thenReturn(Unit)
            `when`(wikiDao.loadTopWikis()).thenReturn(topWikis)
        }
        `when`(responseError.isSuccessful).thenReturn(false)
        `when`(responseSuccess.isSuccessful).thenReturn(true)
        `when`(responseSuccess.body()).thenReturn(mock(ExpandedWikiaResultSet::class.java))
    }

    @Test
    fun localDataEmpty_fetchRemoteSuccess_resultSuccess() = runBlocking {
        `when`(wikiDao.hasTopWikis()).thenReturn(null)
        `when`(fandomService.getTopWikis(anyInt())).thenReturn(responseSuccess)
        assertEquals(TopWikisResult.Success(topWikis), repository.fetchTopWikis())
    }

    @Test
    fun localDataEmpty_fetchRemoteError_resultError() = runBlocking {
        `when`(wikiDao.hasTopWikis()).thenReturn(null)
        `when`(fandomService.getTopWikis(anyInt())).thenReturn(responseError)
        assertEquals(TopWikisResult.Error, repository.fetchTopWikis())
    }

    @Test
    fun localDataExpired_fetchRemoteSuccess_resultSuccess() = runBlocking {
        `when`(wikiDao.hasTopWikis()).thenReturn(topWiki)
        `when`(wikiDao.getTimeCreation()).thenReturn(0L)
        `when`(fandomService.getTopWikis(anyInt())).thenReturn(responseSuccess)
        assertEquals(TopWikisResult.Success(topWikis), repository.fetchTopWikis())
    }

    @Test
    fun localDataExpired_fetchRemoteError_resultErrorRefresh() = runBlocking {
        `when`(wikiDao.hasTopWikis()).thenReturn(topWiki)
        `when`(wikiDao.getTimeCreation()).thenReturn(0L)
        `when`(fandomService.getTopWikis(anyInt())).thenReturn(responseError)
        assertEquals(TopWikisResult.ErrorRefresh(topWikis), repository.fetchTopWikis())
    }

    @Test
    fun localDataValid_forceRefresh_fetchRemoteSuccess_resultSuccess() = runBlocking {
        `when`(wikiDao.hasTopWikis()).thenReturn(topWiki)
        `when`(fandomService.getTopWikis(anyInt())).thenReturn(responseSuccess)
        assertEquals(TopWikisResult.Success(topWikis), repository.fetchTopWikis(true))
    }

    @Test
    fun localDataValid_forceRefresh_fetchRemoteError_resultErrorRefresh() = runBlocking {
        `when`(wikiDao.hasTopWikis()).thenReturn(topWiki)
        `when`(fandomService.getTopWikis(anyInt())).thenReturn(responseError)
        assertEquals(TopWikisResult.ErrorRefresh(topWikis), repository.fetchTopWikis(true))
    }

    @Test
    fun localDataValid_resultSuccess() = runBlocking {
        `when`(wikiDao.hasTopWikis()).thenReturn(topWiki)
        `when`(wikiDao.getTimeCreation()).thenReturn(System.currentTimeMillis())
        assertEquals(TopWikisResult.Success(topWikis), repository.fetchTopWikis())
    }

    private fun <T> any(): T = Mockito.any<T>()
}