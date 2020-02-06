package pl.maciejnowak.exercise.ui.mapper

interface ListMapper<T, K>: Mapper<List<T>, List<K>>

class ListMapperImpl<T, K>(private val mapper: Mapper<T, K>) : ListMapper<T, K> {

    override fun map(input: List<T>): List<K> {
        return input.map { mapper.map(it) }
    }
}