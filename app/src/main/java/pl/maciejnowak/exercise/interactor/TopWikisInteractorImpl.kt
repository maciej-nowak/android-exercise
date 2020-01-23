package pl.maciejnowak.exercise.interactor

import pl.maciejnowak.exercise.model.TopWiki

class TopWikisInteractorImpl : TopWikisInteractor {

    override fun fetch(limit: Int): List<TopWiki>? {
        return mockData()
    }

    private fun mockData(): List<TopWiki> {
        val url = "https://avatars2.githubusercontent.com/u/1171011?s=200&v=4"
        return mutableListOf<TopWiki>().apply {
            add(TopWiki("Title 1", url, 100))
            add(TopWiki("Title 2", url, 200))
            add(TopWiki("Title 3", url, 300))
            add(TopWiki("Title 4", url, 400))
            add(TopWiki("Title 5", url, 500))
        }
    }
}