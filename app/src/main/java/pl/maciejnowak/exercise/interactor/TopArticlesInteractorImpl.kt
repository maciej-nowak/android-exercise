package pl.maciejnowak.exercise.interactor

import pl.maciejnowak.exercise.model.TopArticle

class TopArticlesInteractorImpl : TopArticlesInteractor {

    override fun fetch(limit: Int): List<TopArticle>? {
        return mockData()
    }

    private fun mockData(): List<TopArticle> {
        return mutableListOf<TopArticle>().apply {
            add(TopArticle("Title 1", "User 1", 1578618000000))
            add(TopArticle("Title 2", "User 2", 1578704400000))
            add(TopArticle("Title 3", "User 3", 1578790800000))
            add(TopArticle("Title 4", "User 4", 1578877200000))
            add(TopArticle("Title 5", "User 5", 1578963600000))
        }
    }
}