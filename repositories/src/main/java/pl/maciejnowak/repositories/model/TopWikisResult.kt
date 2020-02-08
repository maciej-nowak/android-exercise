package pl.maciejnowak.repositories.model

import pl.maciejnowak.database.model.TopWiki

sealed class TopWikisResult {

    data class Success(val list: List<TopWiki>): TopWikisResult()
    object Error: TopWikisResult()
    object ErrorRefresh: TopWikisResult()
}