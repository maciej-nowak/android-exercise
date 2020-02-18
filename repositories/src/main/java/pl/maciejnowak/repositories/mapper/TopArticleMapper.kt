package pl.maciejnowak.repositories.mapper

import pl.maciejnowak.commonobjects.entities.TopArticle
import pl.maciejnowak.network.model.ExpandedArticle

class TopArticleMapper {

    fun map(input: ExpandedArticle): TopArticle {
        return with(input) {
            TopArticle(id, title, revision.user, revision.timestamp)
        }
    }
}