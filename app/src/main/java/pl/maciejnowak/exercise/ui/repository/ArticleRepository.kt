package pl.maciejnowak.exercise.ui.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import pl.maciejnowak.exercise.database.ArticleDao
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.Network
import pl.maciejnowak.exercise.network.Result
import pl.maciejnowak.exercise.network.model.ExpandedArticle
import pl.maciejnowak.exercise.ui.mapper.Mapper
import pl.maciejnowak.exercise.ui.viewmodel.model.TopArticlesResult
import java.util.concurrent.TimeUnit

class ArticleRepository(
    private val fandomService: FandomService,
    private val articleDao: ArticleDao,
    private val mapper: Mapper<ExpandedArticle, TopArticle>) {

    val cache: Flow<TopArticlesResult> = articleDao.loadTopArticlesFlow().map { TopArticlesResult.Success(it) }

    fun fetchTopArticlesFlow(): Flow<TopArticlesResult> = flow {
        if(articleDao.hasTopArticles() != null) {
            if(hasDataExpired(articleDao.getTimeCreation()) && !fetchTopArticlesRemote()) {
                emit(TopArticlesResult.ErrorRefresh)
            }
            cache.collect { emit(it) }
        } else {
            if(fetchTopArticlesRemote()) cache.collect { emit(it) } else emit(TopArticlesResult.Error)
        }
    }

    private fun hasDataExpired(creation: Long?): Boolean {
        return (creation == null || creation < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))
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
}