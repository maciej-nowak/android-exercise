package pl.maciejnowak.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.maciejnowak.database.model.TimeCreation
import pl.maciejnowak.database.model.TopArticle
import pl.maciejnowak.database.model.TopWiki

@Database(entities = [TopWiki::class, TopArticle::class, TimeCreation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun wikiDao(): WikiDao
}