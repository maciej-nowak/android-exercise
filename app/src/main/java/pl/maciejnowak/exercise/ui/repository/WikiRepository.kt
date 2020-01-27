package pl.maciejnowak.exercise.ui.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import pl.maciejnowak.exercise.ui.viewmodel.model.Result
import pl.maciejnowak.exercise.database.WikiDao
import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.model.ExpandedWikiaItem
import java.io.IOException
import java.util.concurrent.TimeUnit

class WikiRepository(private val fandomService: FandomService, private val wikiDao: WikiDao) {

    val cache: Flow<Result<List<TopWiki>>> = wikiDao.loadAll().map { Result.Success(it) }

    fun fetchTopWikisLiveData(): LiveData<Result<List<TopWiki>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        if(hasDataExpired(wikiDao.getTimeCreation())) {
            try {
                val response = fandomService.getTopWikis(30)
                if(response.isSuccessful) {
                    response.body()?.items?.run { wikiDao.update(map(ExpandedWikiaItem::toPresentation)) }
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

    //use instead of fetchTopWikisLiveData - is this correct way to manage Flow inside Repository?
    fun fetchTopWikisFlow(): Flow<Result<List<TopWiki>>> = flow {
        emit(Result.Loading())
        if(hasDataExpired(wikiDao.getTimeCreation())) {
            try {
                val response = fandomService.getTopWikis(30)
                if(response.isSuccessful) {
                    response.body()?.items?.run { wikiDao.update(map(ExpandedWikiaItem::toPresentation)) }
                    cache.collect { emit(it) }
                } else {
                    emit(Result.Error())
                }
            } catch (e: IOException) {
                emit(Result.Error())
            }
        } else {
            cache.collect { emit(it) }
        }
    }

    private fun hasDataExpired(creation: Long?): Boolean {
        return (creation == null || creation < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))
    }
}

fun ExpandedWikiaItem.toPresentation(): TopWiki {
    return TopWiki(id, name, image, stats.articles)
}