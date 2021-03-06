package pl.maciejnowak.database.dao

import androidx.room.*
import pl.maciejnowak.commonobjects.entities.TimeCreation
import pl.maciejnowak.commonobjects.entities.TopArticle

@Dao
interface ArticleDao {

    @Query("SELECT * FROM TopArticle")
    suspend fun loadTopArticles(): List<TopArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(articles: List<TopArticle>)

    @Query("DELETE FROM TopArticle")
    suspend fun deleteTopArticles()

    @Query("SELECT * FROM TopArticle LIMIT 1")
    suspend fun hasTopArticles(): TopArticle?

    @Query("SELECT timestamp FROM TimeCreation WHERE table_name LIKE 'TopArticle'")
    suspend fun getTimeCreation(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTimeCreation(timeCreation: TimeCreation)

    @Transaction
    suspend fun update(articles: List<TopArticle>) {
        deleteTopArticles()
        save(articles)
        saveTimeCreation(TimeCreation("TopArticle", System.currentTimeMillis()))
    }
}