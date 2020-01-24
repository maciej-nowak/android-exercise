package pl.maciejnowak.exercise.repository

import pl.maciejnowak.exercise.database.WikiDao
import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.network.FandomService
import pl.maciejnowak.exercise.network.model.ExpandedWikiaItem

class WikiRepository(private val fandomService: FandomService, private val wikiDao: WikiDao) {

    fun getTopWikis(): List<TopWiki>? {
        refreshTopWikis()
        return wikiDao.loadAll()
    }

    private fun refreshTopWikis() {
        //TODO replace by REFRESH_TIMEOUT
        if(wikiDao.hasWikis() == null) {
            val response = fandomService.getTopWikis(30).execute()
            wikiDao.save(response.body()?.items?.map(ExpandedWikiaItem::toPresentation))
        }
    }
}

fun ExpandedWikiaItem.toPresentation(): TopWiki {
    return TopWiki(id, name, image, stats.articles)
}