package pl.maciejnowak.exercise.ui.mapper

import pl.maciejnowak.database.model.TopArticle
import pl.maciejnowak.network.model.ExpandedArticle

class TopArticleMapper : Mapper<ExpandedArticle, TopArticle> {

    override fun map(input: ExpandedArticle): TopArticle {
        return with(input) {
            TopArticle(id, title, revision.user, revision.timestamp)
        }
    }
}