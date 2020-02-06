package pl.maciejnowak.exercise.ui.repository

import kotlinx.coroutines.flow.*
import pl.maciejnowak.exercise.database.WikiDao
import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.Network
import pl.maciejnowak.exercise.network.Result
import pl.maciejnowak.exercise.network.model.ExpandedWikiaItem
import pl.maciejnowak.exercise.ui.mapper.Mapper
import pl.maciejnowak.exercise.ui.viewmodel.model.TopWikisResult
import java.util.concurrent.TimeUnit

class WikiRepository(
    private val fandomService: FandomService,
    private val wikiDao: WikiDao,
    private val mapper: Mapper<ExpandedWikiaItem, TopWiki>) {

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
        return when(val result = Network.invoke { fandomService.getTopWikis(30) }) {
            is Result.Success -> {
                wikiDao.update(result.body.items.map { mapper.map(it) })
                true
            }
            is Result.Error -> false
        }
    }
}

