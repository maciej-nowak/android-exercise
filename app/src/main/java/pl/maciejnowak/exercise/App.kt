package pl.maciejnowak.exercise

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module
import pl.maciejnowak.exercise.ui.viewmodel.TopArticlesViewModel
import pl.maciejnowak.exercise.ui.viewmodel.TopWikisViewModel
import pl.maciejnowak.repositories.di.ModuleRepository

class App : Application() {

    init {
        instance = this
    }

    val appModule = module {
        viewModel { TopWikisViewModel(get()) }
        viewModel { TopArticlesViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            loadKoinModules(ModuleRepository.get())
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