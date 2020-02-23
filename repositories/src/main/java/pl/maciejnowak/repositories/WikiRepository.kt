package pl.maciejnowak.repositories

import pl.maciejnowak.database.dao.WikiDao
import pl.maciejnowak.network.FandomService
import pl.maciejnowak.network.result.Network
import pl.maciejnowak.network.result.Result
import pl.maciejnowak.repositories.mapper.TopWikiMapper
import pl.maciejnowak.repositories.model.TopWikisResult
import java.util.concurrent.TimeUnit

class WikiRepository(
    private val fandomService: FandomService,
    private val wikiDao: WikiDao,
    private val mapper: TopWikiMapper
) {

    suspend fun fetchTopWikis(forceRefresh: Boolean = false): TopWikisResult {
        return if(wikiDao.hasTopWikis() != null) {
            if((forceRefresh || hasDataExpired(wikiDao.getTimeCreation())) && !fetchTopWikisRemote()) {
                TopWikisResult.ErrorRefresh(wikiDao.loadTopWikis())
            } else {
                TopWikisResult.Success(wikiDao.loadTopWikis())
            }
        } else {
            if(fetchTopWikisRemote()) {
                TopWikisResult.Success(wikiDao.loadTopWikis())
            } else {
                TopWikisResult.Error
            }
        }
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

    private fun hasDataExpired(creation: Long?): Boolean {
        return (creation == null || creation < System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1))
    }
}

