package pl.maciejnowak.exercise

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.maciejnowak.database.AppDatabase
import pl.maciejnowak.database.Database
import pl.maciejnowak.network.FandomService
import pl.maciejnowak.repositories.ArticleRepository
import pl.maciejnowak.repositories.WikiRepository
import pl.maciejnowak.repositories.mapper.TopArticleMapper
import pl.maciejnowak.repositories.mapper.TopWikiMapper

class App : Application() {

    init {
        instance = this
    }

    val appModule = module {
        single { FandomService.create() }
        single { Database.create(getContext()) }
        single { get<AppDatabase>().wikiDao() }
        single { get<AppDatabase>().articleDao() }
        factory { WikiRepository(get(), get(), TopWikiMapper()) }
        factory { ArticleRepository(get(), get(), TopArticleMapper()) }
    }

    override fun onCreate() {
        super.onCreate()
        Database.init(getContext())
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }

    companion object {
        lateinit var instance: App
            private set

        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}