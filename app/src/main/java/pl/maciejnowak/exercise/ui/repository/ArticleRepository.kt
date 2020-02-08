package pl.maciejnowak.exercise.ui.repository

import kotlinx.coroutines.flow.*
import pl.maciejnowak.database.ArticleDao
import pl.maciejnowak.database.model.TopArticle
import pl.maciejnowak.network.FandomService
import pl.maciejnowak.network.Network
import pl.maciejnowak.network.Result
import pl.maciejnowak.network.model.ExpandedArticle
import pl.maciejnowak.exercise.ui.mapper.Mapper
import pl.maciejnowak.exercise.ui.viewmodel.model.TopArticlesResult
import java.util.concurrent.TimeUnit

class ArticleRepository(
    private val fandomService: FandomService,
    private val articleDao: ArticleDao,
    private val mapper: Mapper<ExpandedArticle, TopArticle>) {

    val cache: Flow<TopArticlesResult> = articleDao.loadTopArticlesFlow().map { TopArticlesResult.Success(it) }

    fun fetchTopArticlesFlow(forceRefresh: Boolean): Flow<TopArticlesResult> = flow {
        if(articleDao.hasTopArticles() != null) {
            if((forceRefresh || hasDataExpired(articleDao.getTimeCreation())) && !fetchTopArticlesRemote()) {
                emit(TopArticlesResult.ErrorRefresh)
            }
            emitAll(cache)
        } else {
            if(fetchTopArticlesRemote()) emitAll(cache) else emit(TopArticlesResult.Error)
        }
    }

    suspend fun fetchTopArticlesRemote(): Boolean {
        return when(val result = Network.invoke { fandomService.getTopArticles(30) }) {
            is Result.Success -> {
                articleDao.update(result.body.items.map { mapper.map(it) })
                true
            }
            is Result.Error -> false
        }
    }

    private fun hasDataExpired(creation: Long?): Boolean {
        return (creation == null || creation < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))
    }
}