package pl.maciejnowak.exercise.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.maciejnowak.exercise.database.model.TimeCreation
import pl.maciejnowak.exercise.database.model.TopWiki

@Dao
interface WikiDao {

    @Query("SELECT * FROM TopWiki")
    fun loadAll(): Flow<List<TopWiki>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(wikis: List<TopWiki>)

    @Query("DELETE FROM TopWiki")
    suspend fun deleteAll()

    @Query("SELECT timestamp FROM TimeCreation WHERE table_name LIKE 'TopWiki'")
    suspend fun getTimeCreation(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTimeCreation(timeCreation: TimeCreation)

    @Transaction
    suspend fun update(wikis: List<TopWiki>) {
        deleteAll()
        save(wikis)
        saveTimeCreation(TimeCreation("TopWiki", System.currentTimeMillis()))
    }
}