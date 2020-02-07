package pl.maciejnowak.exercise.ui.mapper

import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.network.model.ExpandedArticle

class TopArticleMapper : Mapper<ExpandedArticle, TopArticle> {

    override fun map(input: ExpandedArticle): TopArticle {
        return with(input) {
            TopArticle(id, title, revision.user, revision.timestamp)
        }
    }
}