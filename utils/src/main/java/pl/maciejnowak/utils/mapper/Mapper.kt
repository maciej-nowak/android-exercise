package pl.maciejnowak.utils.mapper

interface Mapper<T, K> {

    fun map(input: T): K
}