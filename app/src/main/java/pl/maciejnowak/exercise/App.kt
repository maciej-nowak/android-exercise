package pl.maciejnowak.exercise

import android.app.Application
import android.content.Context
import pl.maciejnowak.database.Database

class App : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Database.init(getContext())
    }

    companion object {
        lateinit var instance: App
            private set

        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}