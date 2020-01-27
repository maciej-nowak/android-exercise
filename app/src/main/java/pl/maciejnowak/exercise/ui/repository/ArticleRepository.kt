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
import java.io.IOException
import java.util.concurrent.TimeUnit

class ArticleRepository(private val fandomService: FandomService, private val articleDao: ArticleDao) {
    
    val cache: Flow<Result<List<TopArticle>>> = articleDao.loadAll().map { Result.Success(it) }

    fun fetchTopArticlesLiveData(): LiveData<Result<List<TopArticle>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        if(hasDataExpired(articleDao.getTimeCreation())) {
            try {
                val response = fandomService.getTopArticles(30)
                if(response.isSuccessful) {
                    response.body()?.items?.run { articleDao.save(map(ExpandedArticle::toPresentation)) }
                    emitSource(cache.asLiveData())
                } else {
                    emit(Result.Error())
                }
            } catch (e: IOException) {
                emit(Result.Error())
            }
        } else {
            emitSource(cache.asLiveData())
        }
    }

    private fun hasDataExpired(creation: Long?): Boolean {
        return (creation == null || creation < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))
    }
}

fun ExpandedArticle.toPresentation(): TopArticle {
    return TopArticle(id, title, revision.user, revision.timestamp)
}