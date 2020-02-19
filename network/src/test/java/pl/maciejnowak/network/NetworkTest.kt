package pl.maciejnowak.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import pl.maciejnowak.network.model.ExpandedWikiaResultSet
import pl.maciejnowak.network.result.Network
import pl.maciejnowak.network.result.Result
import retrofit2.Response
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class NetworkTest {

    @Mock private lateinit var fandomService: FandomService
    @Mock private lateinit var response: Response<ExpandedWikiaResultSet>
    @Mock private lateinit var responseBody: ExpandedWikiaResultSet

    @Test
    fun responseSuccessful_hasBody_resultSuccess() = runBlocking {
        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.body()).thenReturn(responseBody)
        `when`(fandomService.getTopWikis(anyInt())).thenReturn(response)
        val result = Network.invoke { fandomService.getTopWikis(anyInt()) }
        assertEquals(Result.Success(responseBody), result)
    }

    @Test
    fun responseSuccessful_noBody_resultErrorWithCode() = runBlocking {
        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.body()).thenReturn(null)
        `when`(response.code()).thenReturn(0)
        `when`(fandomService.getTopWikis(anyInt())).thenReturn(response)
        val result = Network.invoke { fandomService.getTopWikis(anyInt()) }
        assertEquals(Result.Error(0), result)
    }

    @Test
    fun responseUnsuccessful_resultErrorWithCode() = runBlocking {
        `when`(response.isSuccessful).thenReturn(false)
        `when`(response.code()).thenReturn(0)
        `when`(fandomService.getTopWikis(anyInt())).thenReturn(response)
        val result = Network.invoke { fandomService.getTopWikis(anyInt()) }
        assertEquals(Result.Error(0), result)
    }

    @Test
    fun networkCallThrowException_resultError() = runBlocking {
        `when`(fandomService.getTopWikis(anyInt())).thenAnswer { throw IOException() }
        val result = Network.invoke { fandomService.getTopWikis(anyInt()) }
        assertEquals(Result.Error(), result)
    }
}