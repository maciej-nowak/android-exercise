package pl.maciejnowak.exercise.database

import androidx.room.Room
import pl.maciejnowak.exercise.App

object Database {

    private val appDatabase = Room
        .databaseBuilder(App.getContext(), AppDatabase::class.java, "AppDatabase")
        .build()

    val wikiDao = appDatabase.wikiDao()
    val articleDao = appDatabase.articleDao()
}