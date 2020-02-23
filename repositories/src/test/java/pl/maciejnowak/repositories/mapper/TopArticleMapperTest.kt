package pl.maciejnowak.repositories.mapper

import org.junit.Assert.*
import org.junit.Test
import pl.maciejnowak.network.model.ExpandedArticle
import pl.maciejnowak.network.model.Revision
import pl.maciejnowak.repositories.mapper.TopArticleMapper

class TopArticleMapperTest {

    private val mapper = TopArticleMapper()

    @Test
    fun validExpandedArticle_map_success() {
        val input = ExpandedArticle(0, "title", Revision("user", 0L))
        val output = mapper.map(input)
        assertEquals(input.id, output.id)
        assertEquals(input.title, output.title)
        assertEquals(input.revision.user, output.user)
        assertEquals(input.revision.timestamp, output.timestamp)
    }

    @Test(expected = KotlinNullPointerException::class)
    fun invalidExpandedArticle_map_throwException() {
        val input: ExpandedArticle? = null
        val output = mapper.map(input!!)
    }
}