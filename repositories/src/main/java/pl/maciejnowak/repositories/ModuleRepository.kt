package pl.maciejnowak.repositories

import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import pl.maciejnowak.database.ModuleDatabase
import pl.maciejnowak.network.ModuleNetwork
import pl.maciejnowak.repositories.mapper.TopArticleMapper
import pl.maciejnowak.repositories.mapper.TopWikiMapper

object ModuleRepository {

    fun get() = module {
        loadKoinModules(listOf(ModuleNetwork.get(), ModuleDatabase.get()))
        factory { WikiRepository(get(), get(), TopWikiMapper()) }
        factory { ArticleRepository(get(), get(), TopArticleMapper()) }
    }
}