package pl.maciejnowak.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopArticle(
    @PrimaryKey val id: Int,
    val title: String,
    val user: String,
    val timestamp: Long
)