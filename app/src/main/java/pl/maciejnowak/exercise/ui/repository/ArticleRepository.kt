package pl.maciejnowak.exercise.ui.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.maciejnowak.exercise.ui.viewmodel.model.Result
import pl.maciejnowak.exercise.database.ArticleDao
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.model.ExpandedArticle
import pl.maciejnowak.exercise.ui.viewmodel.model.ErrorType
import java.io.IOException
import java.util.concurrent.TimeUnit

class ArticleRepository(private val fandomService: FandomService, private val articleDao: ArticleDao) {
    
    val cache: Flow<Result<List<TopArticle>>> = articleDao.loadTopArticlesFlow().map { Result.Success(it) }

    fun fetchTopArticlesLiveData(): LiveData<Result<List<TopArticle>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        if(articleDao.hasTopArticles() != null) {
            emitSource(cache.asLiveData())
            if(hasDataExpired(articleDao.getTimeCreation()) && !fetchTopArticlesRemote()) {
                emit(Result.Error(ErrorType.REFRESH))
                emitSource(cache.asLiveData())
            }
        } else {
            if(fetchTopArticlesRemote()) emitSource(cache.asLiveData()) else emit(Result.Error())
        }
    }

    private fun hasDataExpired(creation: Long?): Boolean {
        return (creation == null || creation < System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1))
    }

    private suspend fun fetchTopArticlesRemote(): Boolean {
        return try {
            val response = fandomService.getTopArticles(30)
            if(response.isSuccessful) {
                response.body()?.items?.run { articleDao.update(map(ExpandedArticle::toPresentation)) }
                true
            } else {
                false
            }
        } catch (e: IOException) {
            false
        }
    }
}

fun ExpandedArticle.toPresentation(): TopArticle {
    return TopArticle(id, title, revision.user, revision.timestamp)
}