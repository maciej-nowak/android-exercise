package pl.maciejnowak.network.result

import retrofit2.Response
import java.io.IOException

object Network {

    suspend fun <T> invoke(endpoint: suspend () -> Response<T>): Result<T> {
        return try {
            val response = endpoint.invoke()
            if(response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(response.code())
            }
        } catch (e: IOException) {
            Result.Error()
        }
    }
}