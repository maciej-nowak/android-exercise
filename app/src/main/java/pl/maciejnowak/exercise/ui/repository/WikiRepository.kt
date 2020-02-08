package pl.maciejnowak.exercise.ui.repository

import kotlinx.coroutines.flow.*
import pl.maciejnowak.database.WikiDao
import pl.maciejnowak.database.model.TopWiki
import pl.maciejnowak.network.FandomService
import pl.maciejnowak.network.Network
import pl.maciejnowak.network.Result
import pl.maciejnowak.network.model.ExpandedWikiaItem
import pl.maciejnowak.exercise.ui.mapper.Mapper
import pl.maciejnowak.exercise.ui.viewmodel.model.TopWikisResult
import java.util.concurrent.TimeUnit

class WikiRepository(
    private val fandomService: FandomService,
    private val wikiDao: WikiDao,
    private val mapper: Mapper<ExpandedWikiaItem, TopWiki>) {

    val cache: Flow<TopWikisResult> = wikiDao.loadTopWikisFlow().map { TopWikisResult.Success(it) }

    fun fetchTopWikisFlow(forceRefresh: Boolean): Flow<TopWikisResult> = flow {
        if(wikiDao.hasTopWikis() != null) {
            if((forceRefresh || hasDataExpired(wikiDao.getTimeCreation())) && !fetchTopWikisRemote()) {
                emit(TopWikisResult.ErrorRefresh)
            }
            emitAll(cache)
        } else {
            if(fetchTopWikisRemote()) emitAll(cache) else emit(TopWikisResult.Error)
        }
    }

    suspend fun fetchTopWikisRemote(): Boolean {
        return when(val result = Network.invoke { fandomService.getTopWikis(30) }) {
            is Result.Success -> {
                wikiDao.update(result.body.items.map { mapper.map(it) })
                true
            }
            is Result.Error -> false
        }
    }

    private fun hasDataExpired(creation: Long?): Boolean {
        return (creation == null || creation < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1))
    }
}

