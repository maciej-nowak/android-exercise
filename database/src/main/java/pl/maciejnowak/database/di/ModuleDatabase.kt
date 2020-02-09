package pl.maciejnowak.database.di

import org.koin.dsl.module
import pl.maciejnowak.database.AppDatabase

object ModuleDatabase {

    fun get() = module {
        single { AppDatabase.create(get()) }
        single { get<AppDatabase>().wikiDao() }
        single { get<AppDatabase>().articleDao() }
    }
}