package pl.maciejnowak.repositories.mapper

import pl.maciejnowak.commonobjects.entities.TopWiki
import pl.maciejnowak.network.model.ExpandedWikiaItem

class TopWikiMapper {

    fun map(input: ExpandedWikiaItem): TopWiki {
        return with(input) {
            TopWiki(id, name, image, stats.articles)
        }
    }
}