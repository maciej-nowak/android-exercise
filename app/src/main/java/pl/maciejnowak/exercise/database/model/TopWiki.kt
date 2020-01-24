package pl.maciejnowak.exercise.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topwiki")
data class TopWiki(
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String,
    val articlesCounter: Int
)