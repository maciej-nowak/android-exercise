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

class ArticleRepository(private val fandomService: FandomService, private val articleDao: ArticleDao) {

    val cache: LiveData<Resource<List<TopArticle>>> = articleDao.loadAll().map { Resource.Success(it) }

    fun fetchTopArticlesLiveData(): LiveData<Resource<List<TopArticle>>> = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        //TODO replace by REFRESH_TIMEOUT
        if(articleDao.hasArticles() == null) {
            try {
                val response = fandomService.getTopArticles(30).execute()
                if(response.isSuccessful) {
                    response.body()?.items?.run { articleDao.save(map(ExpandedArticle::toPresentation)) }
                    emitSource(cache)
                } else {
                    emit(Resource.Error(message = response.message()))
                }
            } catch (e: IOException) {
                emit(Resource.Error())
            }
        } else {
            emitSource(cache)
        }
    }
}

fun ExpandedArticle.toPresentation(): TopArticle {
    return TopArticle(id, title, revision.user, revision.timestamp)
}