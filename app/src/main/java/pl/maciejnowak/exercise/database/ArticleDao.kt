package pl.maciejnowak.exercise.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.maciejnowak.exercise.database.model.TopArticle

@Dao
interface ArticleDao {

    @Query("SELECT * FROM toparticle")
    fun loadAll(): LiveData<List<TopArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(articles: List<TopArticle>)

    @Query("DELETE FROM toparticle")
    fun deleteAll()

    @Query("SELECT * FROM toparticle LIMIT 1")
    fun hasArticles(): TopArticle?
}