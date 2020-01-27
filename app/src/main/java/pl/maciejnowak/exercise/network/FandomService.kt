package pl.maciejnowak.exercise.network

import pl.maciejnowak.exercise.network.model.ExpandedArticleResultSet
import pl.maciejnowak.exercise.network.model.ExpandedWikiaResultSet
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FandomService  {

    @GET("Wikis/List?expand=1")
    suspend fun getTopWikis(@Query("limit") limit: Int): Response<ExpandedWikiaResultSet>

    @GET("Articles/Top?expand=1")
    suspend fun getTopArticles(@Query("limit") limit: Int): Response<ExpandedArticleResultSet>

    companion object {
        private const val BASE_URL = "https://community.fandom.com/api/v1/"

        fun create(): FandomService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create<FandomService>(FandomService::class.java)
        }
    }
}