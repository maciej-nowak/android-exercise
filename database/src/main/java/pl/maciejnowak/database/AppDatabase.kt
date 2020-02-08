package pl.maciejnowak.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.maciejnowak.commonobjects.entities.TimeCreation
import pl.maciejnowak.commonobjects.entities.TopArticle
import pl.maciejnowak.commonobjects.entities.TopWiki

@Database(entities = [TopWiki::class, TopArticle::class, TimeCreation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun wikiDao(): WikiDao
}