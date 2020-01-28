package pl.maciejnowak.exercise

import android.app.Application
import android.content.Context

class App : Application() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set

        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}