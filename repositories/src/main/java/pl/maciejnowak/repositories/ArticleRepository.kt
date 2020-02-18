package pl.maciejnowak.repositories

import pl.maciejnowak.database.dao.ArticleDao
import pl.maciejnowak.commonobjects.entities.TopArticle
import pl.maciejnowak.network.FandomService
import pl.maciejnowak.network.result.Network
import pl.maciejnowak.network.result.Result
import pl.maciejnowak.network.model.ExpandedArticle
import pl.maciejnowak.utils.mapper.Mapper
import pl.maciejnowak.repositories.model.TopArticlesResult
import java.util.concurrent.TimeUnit

class ArticleRepository(
    private val fandomService: FandomService,
    private val articleDao: ArticleDao,
    private val mapper: Mapper<ExpandedArticle, TopArticle>
) {

    suspend fun fetchTopArticles(forceRefresh: Boolean): TopArticlesResult {
        return if(articleDao.hasTopArticles() != null) {
            if((forceRefresh || hasDataExpired(articleDao.getTimeCreation())) && !fetchTopArticlesRemote()) {
                TopArticlesResult.ErrorRefresh(articleDao.loadTopArticles())
            } else {
                TopArticlesResult.Success(articleDao.loadTopArticles())
            }
        } else {
            if(fetchTopArticlesRemote()) {
                TopArticlesResult.Success(articleDao.loadTopArticles())
            } else {
                TopArticlesResult.Error
            }
        }
    }

    private suspend fun fetchTopArticlesRemote(): Boolean {
        return when(val result = Network.invoke { fandomService.getTopArticles(30) }) {
            is Result.Success -> {
                articleDao.update(result.body.items.map { mapper.map(it) })
                true
            }
            is Result.Error -> false
        }
    }

    private fun hasDataExpired(creation: Long?): Boolean {
        return (creation == null || creation < System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1))
    }
}