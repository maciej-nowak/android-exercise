package pl.maciejnowak.repositories.model

import pl.maciejnowak.commonobjects.entities.TopWiki

sealed class TopWikisResult {

    data class Success(val list: List<TopWiki>): TopWikisResult()
    object Error: TopWikisResult()
    object ErrorRefresh: TopWikisResult()
}