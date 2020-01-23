package pl.maciejnowak.exercise.interactor

import pl.maciejnowak.exercise.network.FandomApiService
import pl.maciejnowak.exercise.network.model.ExpandedArticleResultSet
import java.io.IOException

class TopArticlesInteractorImpl : TopArticlesInteractor {

    private val service by lazy { FandomApiService.create() }

    override fun fetch(limit: Int): ExpandedArticleResultSet?{
        return try {
            val response = service.getTopArticles(limit).execute()
            if(response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }
}