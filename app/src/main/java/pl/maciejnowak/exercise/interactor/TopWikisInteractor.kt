package pl.maciejnowak.exercise.interactor

import pl.maciejnowak.exercise.network.model.ExpandedWikiaResultSet

interface TopWikisInteractor {

    fun fetch(limit: Int = 30): ExpandedWikiaResultSet?
}