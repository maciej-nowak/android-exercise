package pl.maciejnowak.repositories

import org.junit.Assert.*
import org.junit.Test
import pl.maciejnowak.network.model.ExpandedWikiaItem
import pl.maciejnowak.network.model.WikiaStats
import pl.maciejnowak.repositories.mapper.TopWikiMapper

class TopWikiMapperTest {

    private val mapper = TopWikiMapper()

    @Test
    fun validExpandendWikiaItem_map_success() {
        val input = ExpandedWikiaItem(0, "name", "image", WikiaStats(0))
        val output = mapper.map(input)
        assertEquals(input.id, output.id)
        assertEquals(input.name, output.title)
        assertEquals(input.image, output.imageUrl)
        assertEquals(input.stats.articles, output.articlesCounter)
    }

    @Test(expected = KotlinNullPointerException::class)
    fun invalidExpandedWikiaItem_map_throwException() {
        val input: ExpandedWikiaItem? = null
        val output = mapper.map(input!!)
    }
}