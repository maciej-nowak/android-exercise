package pl.maciejnowak.commonobjects.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopWiki(
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "articles_counter") val articlesCounter: Int
)