package pl.maciejnowak.exercise.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import pl.maciejnowak.exercise.Resource
import pl.maciejnowak.exercise.database.WikiDao
import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.model.ExpandedWikiaItem
import java.io.IOException
import java.util.concurrent.TimeUnit

class WikiRepository(private val fandomService: FandomService, private val wikiDao: WikiDao) {

    val cache: LiveData<Resource<List<TopWiki>>> = wikiDao.loadAll().map { Resource.Success(it) }

    fun fetchTopWikisLiveData() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        if(hasDataExpired(wikiDao.getTimeCreation())) {
            try {
                val response = fandomService.getTopWikis(30).execute()
                if(response.isSuccessful) {
                    response.body()?.items?.run { wikiDao.save(map(ExpandedWikiaItem::toPresentation)) }
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

fun ExpandedWikiaItem.toPresentation(): TopWiki {
    return TopWiki(id, name, image, stats.articles)
}