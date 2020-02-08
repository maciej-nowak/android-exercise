package pl.maciejnowak.exercise.ui.viewmodel.model

import pl.maciejnowak.database.model.TopArticle

sealed class TopArticlesResult {

    data class Success(val list: List<TopArticle>): TopArticlesResult()
    object Error: TopArticlesResult()
    object ErrorRefresh: TopArticlesResult()
}