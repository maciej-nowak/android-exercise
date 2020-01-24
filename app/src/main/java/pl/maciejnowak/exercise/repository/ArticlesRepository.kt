package pl.maciejnowak.exercise.repository

import pl.maciejnowak.exercise.database.ArticleDao
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.model.ExpandedArticle

class ArticleRepository(private val fandomService: FandomService, private val articleDao: ArticleDao) {

    fun getTopArticles(): List<TopArticle>? {
        refreshTopArticles()
        return articleDao.loadAll()
    }

    private fun refreshTopArticles() {
        //TODO replace by REFRESH_TIMEOUT
        if(articleDao.hasArticles() == null) {
            val response = fandomService.getTopArticles(30).execute()
            articleDao.save(response.body()?.items?.map(ExpandedArticle::toPresentation))
        }
    }
}

fun ExpandedArticle.toPresentation(): TopArticle {
    return TopArticle(id, title, revision.user, revision.timestamp)
}