package pl.maciejnowak.commonobjects.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopArticle(
    @PrimaryKey val id: Int,
    val title: String,
    val user: String,
    val timestamp: Long
)