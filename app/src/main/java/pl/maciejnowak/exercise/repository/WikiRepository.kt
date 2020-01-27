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

class WikiRepository(private val fandomService: FandomService, private val wikiDao: WikiDao) {

    val cache: LiveData<Resource<List<TopWiki>>> = wikiDao.loadAll().map { Resource.Success(it) }

    fun fetchTopWikisLiveData(): LiveData<Resource<List<TopWiki>>> = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        //TODO replace by REFRESH_TIMEOUT
        if(wikiDao.hasWikis() == null) {
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
}

fun ExpandedWikiaItem.toPresentation(): TopWiki {
    return TopWiki(id, name, image, stats.articles)
}