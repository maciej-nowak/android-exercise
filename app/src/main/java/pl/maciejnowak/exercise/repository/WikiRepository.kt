package pl.maciejnowak.exercise.repository

import pl.maciejnowak.exercise.database.WikiDao
import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.model.ExpandedWikiaItem
import java.io.IOException

class WikiRepository(private val fandomService: FandomService, private val wikiDao: WikiDao) {

    fun getTopWikis(): List<TopWiki>? {
        refreshTopWikis()
        return wikiDao.loadAll()
    }

    private fun refreshTopWikis() {
        //TODO replace by REFRESH_TIMEOUT
        if(wikiDao.hasWikis() == null) {
            try {
                val response = fandomService.getTopWikis(30).execute()
                if(response.isSuccessful) {
                    response.body()?.items?.run { wikiDao.save(map(ExpandedWikiaItem::toPresentation)) }
                } else {
                    //TODO error state
                }
            } catch (e: IOException) {
                //TODO error state
            }
        }
    }
}

fun ExpandedWikiaItem.toPresentation(): TopWiki {
    return TopWiki(id, name, image, stats.articles)
}