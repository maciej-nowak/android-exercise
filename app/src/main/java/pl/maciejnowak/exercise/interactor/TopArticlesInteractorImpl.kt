package pl.maciejnowak.exercise.interactor

import pl.maciejnowak.exercise.network.Network
import pl.maciejnowak.exercise.network.model.ExpandedArticleResultSet
import java.io.IOException

class TopArticlesInteractorImpl : TopArticlesInteractor {

    override fun fetch(limit: Int): ExpandedArticleResultSet?{
        return try {
            val response = Network.fandomService.getTopArticles(limit).execute()
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