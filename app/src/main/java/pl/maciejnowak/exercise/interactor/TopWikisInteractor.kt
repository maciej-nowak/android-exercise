package pl.maciejnowak.exercise.interactor

import pl.maciejnowak.exercise.model.TopWiki

interface TopWikisInteractor {

    fun fetch(limit: Int = 30): List<TopWiki>?
}