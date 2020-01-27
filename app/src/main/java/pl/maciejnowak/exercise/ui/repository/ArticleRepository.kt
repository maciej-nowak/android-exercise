package pl.maciejnowak.exercise.ui.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import pl.maciejnowak.exercise.ui.viewmodel.model.Result
import pl.maciejnowak.exercise.database.ArticleDao
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.model.ExpandedArticle
import java.io.IOException
import java.util.concurrent.TimeUnit

class ArticleRepository(private val fandomService: FandomService, private val articleDao: ArticleDao) {

    val cache: LiveData<Result<List<TopArticle>>> = articleDao.loadAll().map { Result.Success(it) }

    fun fetchTopArticlesLiveData() = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        if(hasDataExpired(articleDao.getTimeCreation())) {
            try {
                val response = fandomService.getTopArticles(30).execute()
                if(response.isSuccessful) {
                    response.body()?.items?.run { articleDao.save(map(ExpandedArticle::toPresentation)) }
                    emitSource(cache)
                } else {
                    emit(Result.Error())
                }
            } catch (e: IOException) {
                emit(Result.Error())
            }
        } else {
            emitSource(cache)
        }
    }

    private fun hasDataExpired(creation: Long?): Boolean {
        return (creation == null || creation < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))
    }
}

fun ExpandedArticle.toPresentation(): TopArticle {
    return TopArticle(id, title, revision.user, revision.timestamp)
}