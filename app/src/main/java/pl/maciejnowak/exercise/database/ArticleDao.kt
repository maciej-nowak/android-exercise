package pl.maciejnowak.exercise.database

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.maciejnowak.exercise.database.model.TimeCreation
import pl.maciejnowak.exercise.database.model.TopArticle

@Dao
interface ArticleDao {

    @Query("SELECT * FROM TopArticle")
    fun loadAll(): LiveData<List<TopArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(articles: List<TopArticle>)

    @Query("DELETE FROM TopArticle")
    suspend fun deleteAll()

    @Query("SELECT timestamp FROM TimeCreation WHERE table_name LIKE 'TopArticle'")
    fun getTimeCreation(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTimeCreation(timeCreation: TimeCreation)

    @Transaction
    suspend fun update(articles: List<TopArticle>) {
        deleteAll()
        save(articles)
        saveTimeCreation(TimeCreation("TopArticle", System.currentTimeMillis()))
    }
}