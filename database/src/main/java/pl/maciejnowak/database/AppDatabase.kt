package pl.maciejnowak.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.maciejnowak.commonobjects.entities.TimeCreation
import pl.maciejnowak.commonobjects.entities.TopArticle
import pl.maciejnowak.commonobjects.entities.TopWiki
import pl.maciejnowak.database.dao.ArticleDao
import pl.maciejnowak.database.dao.WikiDao

@Database(entities = [TopWiki::class, TopArticle::class, TimeCreation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun wikiDao(): WikiDao

    companion object {
        fun create(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, "AppDatabase")
                .build()
        }
    }
}