package pl.maciejnowak.exercise.interactor

import pl.maciejnowak.exercise.model.TopArticle

interface TopArticlesInteractor {

    fun fetch(limit: Int = 30): List<TopArticle>?
}