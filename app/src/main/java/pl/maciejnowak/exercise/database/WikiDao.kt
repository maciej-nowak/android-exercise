package pl.maciejnowak.exercise.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.maciejnowak.exercise.database.model.TimeCreation
import pl.maciejnowak.exercise.database.model.TopWiki

@Dao
interface WikiDao {

    @Query("SELECT * FROM TopWiki")
    fun loadTopWikisFlow(): Flow<List<TopWiki>>

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