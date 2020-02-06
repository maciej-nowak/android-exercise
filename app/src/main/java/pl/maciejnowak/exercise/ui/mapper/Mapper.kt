package pl.maciejnowak.exercise.ui.mapper

interface Mapper<T, K> {

    fun map(input: T): K
}