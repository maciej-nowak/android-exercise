package pl.maciejnowak.exercise.repository

import pl.maciejnowak.exercise.database.ArticleDao
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.model.ExpandedArticle
import java.io.IOException

class ArticleRepository(private val fandomService: FandomService, private val articleDao: ArticleDao) {

    fun getTopArticles(): List<TopArticle>? {
        refreshTopArticles()
        return articleDao.loadAll()
    }

    private fun refreshTopArticles() {
        //TODO replace by REFRESH_TIMEOUT
        if(articleDao.hasArticles() == null) {
            try {
                val response = fandomService.getTopArticles(30).execute()
                if(response.isSuccessful) {
                    response.body()?.items?.run { articleDao.save(map(ExpandedArticle::toPresentation)) }
                } else {
                    //TODO error state
                }
            } catch (e: IOException) {
                //TODO error state
            }
        }
    }
}

fun ExpandedArticle.toPresentation(): TopArticle {
    return TopArticle(id, title, revision.user, revision.timestamp)
}