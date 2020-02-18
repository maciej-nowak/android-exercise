package pl.maciejnowak.repositories.model

import pl.maciejnowak.commonobjects.entities.TopWiki

sealed class TopWikisResult {

    data class Success(val list: List<TopWiki>): TopWikisResult()
    data class ErrorRefresh(val list: List<TopWiki>): TopWikisResult()
    object Error: TopWikisResult()
}