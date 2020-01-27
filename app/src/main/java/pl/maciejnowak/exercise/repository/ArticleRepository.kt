package pl.maciejnowak.exercise.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import pl.maciejnowak.exercise.Resource
import pl.maciejnowak.exercise.database.ArticleDao
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.model.ExpandedArticle
import java.io.IOException
import java.util.concurrent.TimeUnit

class ArticleRepository(private val fandomService: FandomService, private val articleDao: ArticleDao) {

    val cache: LiveData<Resource<List<TopArticle>>> = articleDao.loadAll().map { Resource.Success(it) }

    fun fetchTopArticlesLiveData() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        if(hasDataExpired(articleDao.getTimeCreation())) {
            try {
                val response = fandomService.getTopArticles(30).execute()
                if(response.isSuccessful) {
                    response.body()?.items?.run { articleDao.save(map(ExpandedArticle::toPresentation)) }
                    emitSource(cache)
                } else {
                    emit(Resource.Error())
                }
            } catch (e: IOException) {
                emit(Resource.Error())
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