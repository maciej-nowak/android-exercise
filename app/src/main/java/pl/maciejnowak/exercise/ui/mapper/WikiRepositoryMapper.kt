package pl.maciejnowak.exercise.ui.mapper

import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.network.model.ExpandedWikiaItem

class WikiRepositoryMapper : Mapper<ExpandedWikiaItem, TopWiki> {

    override fun map(input: ExpandedWikiaItem): TopWiki {
        return with(input) {
            TopWiki(id, name, image, stats.articles)
        }
    }
}