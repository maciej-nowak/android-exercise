package pl.maciejnowak.repositories.model

import pl.maciejnowak.commonobjects.entities.TopArticle

sealed class TopArticlesResult {

    data class Success(val list: List<TopArticle>): TopArticlesResult()
    data class ErrorRefresh(val list: List<TopArticle>): TopArticlesResult()
    object Error: TopArticlesResult()
}