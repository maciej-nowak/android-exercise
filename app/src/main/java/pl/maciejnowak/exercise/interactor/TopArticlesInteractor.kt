package pl.maciejnowak.exercise.interactor

import pl.maciejnowak.exercise.network.model.ExpandedArticleResultSet

interface TopArticlesInteractor {

    fun fetch(limit: Int = 30): ExpandedArticleResultSet?
}