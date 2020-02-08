package pl.maciejnowak.database

import org.koin.dsl.module

object ModuleDatabase {

    fun get() = module {
        single { Database.create(get()) }
        single { get<AppDatabase>().wikiDao() }
        single { get<AppDatabase>().articleDao() }
    }
}