package pl.maciejnowak.network.di

import org.koin.dsl.module
import pl.maciejnowak.network.FandomService

object ModuleNetwork {

    fun get() = module {
        single { FandomService.create() }
    }
}