package pl.maciejnowak.exercise.network

import pl.maciejnowak.exercise.network.model.ExpandedArticleResultSet
import pl.maciejnowak.exercise.network.model.ExpandedWikiaResultSet
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FandomApiService  {

    @GET("Wikis/List?expand=1")
    fun getTopWikis(@Query("limit") limit: Int): Call<ExpandedWikiaResultSet>

    @GET("Articles/Top?expand=1")
    fun getTopArticles(@Query("limit") limit: Int): Call<ExpandedArticleResultSet>

    companion object {
        private const val BASE_URL = "https://community.fandom.com/api/v1/"

        fun create(): FandomApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create<FandomApiService>(FandomApiService::class.java)
        }
    }
}