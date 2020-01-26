package pl.maciejnowak.exercise.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.maciejnowak.exercise.database.model.TopWiki

@Dao
interface WikiDao {

    @Query("SELECT * FROM topwiki")
    fun loadAll(): LiveData<List<TopWiki>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(wikis: List<TopWiki>)

    @Query("DELETE FROM topwiki")
    fun deleteAll()

    @Query("SELECT * FROM topwiki LIMIT 1")
    fun hasWikis(): TopWiki?
}