package pl.maciejnowak.repositories.mapper

import pl.maciejnowak.commonobjects.entities.TopArticle
import pl.maciejnowak.network.model.ExpandedArticle
import pl.maciejnowak.utils.mapper.Mapper

class TopArticleMapper : Mapper<ExpandedArticle, TopArticle> {

    override fun map(input: ExpandedArticle): TopArticle {
        return with(input) {
            TopArticle(id, title, revision.user, revision.timestamp)
        }
    }
}