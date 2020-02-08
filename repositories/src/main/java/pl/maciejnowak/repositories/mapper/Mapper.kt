package pl.maciejnowak.repositories.mapper

interface Mapper<T, K> {

    fun map(input: T): K
}