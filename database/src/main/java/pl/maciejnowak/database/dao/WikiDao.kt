package pl.maciejnowak.database.dao

import androidx.room.*
import pl.maciejnowak.commonobjects.entities.TimeCreation
import pl.maciejnowak.commonobjects.entities.TopWiki

@Dao
interface WikiDao {

    @Query("SELECT * FROM TopWiki")
    suspend fun loadTopWikis(): List<TopWiki>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(wikis: List<TopWiki>)

    @Query("DELETE FROM TopWiki")
    suspend fun deleteTopWikis()

    @Query("SELECT * FROM TopWiki LIMIT 1")
    suspend fun hasTopWikis(): TopWiki?

    @Query("SELECT timestamp FROM TimeCreation WHERE table_name LIKE 'TopWiki'")
    suspend fun getTimeCreation(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTimeCreation(timeCreation: TimeCreation)

    @Transaction
    suspend fun update(wikis: List<TopWiki>) {
        deleteTopWikis()
        save(wikis)
        saveTimeCreation(TimeCreation("TopWiki", System.currentTimeMillis()))
    }
}