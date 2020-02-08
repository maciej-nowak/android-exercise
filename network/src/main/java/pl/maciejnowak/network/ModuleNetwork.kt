package pl.maciejnowak.network

import org.koin.dsl.module

object ModuleNetwork {

    fun get() = module {
        single { FandomService.create() }
    }
}