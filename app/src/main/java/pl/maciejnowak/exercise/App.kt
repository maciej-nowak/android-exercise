package pl.maciejnowak.exercise

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.maciejnowak.database.Database

class App : Application() {

    init {
        instance = this
    }

    val appModule = module {

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