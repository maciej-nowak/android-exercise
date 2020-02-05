package pl.maciejnowak.exercise.ui.repository

import kotlinx.coroutines.flow.*
import pl.maciejnowak.exercise.database.WikiDao
import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.model.ExpandedWikiaItem
import pl.maciejnowak.exercise.ui.viewmodel.model.TopWikisResult
import java.io.IOException
import java.util.concurrent.TimeUnit

class WikiRepository(private val fandomService: FandomService, private val wikiDao: WikiDao) {

    val cache: Flow<TopWikisResult> = wikiDao.loadTopWikisFlow().map { TopWikisResult.Success(it) }

    fun fetchTopWikisFlow(): Flow<TopWikisResult> = flow {
        if(wikiDao.hasTopWikis() != null) {
            if(hasDataExpired(wikiDao.getTimeCreation()) && !fetchTopWikisRemote()) {
                emit(TopWikisResult.ErrorRefresh)
            }
            cache.collect { emit(it) }
        } else {
            if(fetchTopWikisRemote()) cache.collect { emit(it) } else emit(TopWikisResult.Error)
        }
    }

    private fun hasDataExpired(creation: Long?): Boolean {
        return (creation == null || creation < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))
    }

    private suspend fun fetchTopWikisRemote(): Boolean {
        return try {
            val response = fandomService.getTopWikis(30)
            if(response.isSuccessful) {
                response.body()?.items?.run { wikiDao.update(map(ExpandedWikiaItem::toPresentation)) }
                true
            } else {
                false
            }
        } catch (e: IOException) {
            false
        }
    }
}

fun ExpandedWikiaItem.toPresentation(): TopWiki {
    return TopWiki(id, name, image, stats.articles)
}