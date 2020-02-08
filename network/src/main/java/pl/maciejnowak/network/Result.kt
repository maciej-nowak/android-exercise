package pl.maciejnowak.network

sealed class Result<out T> {
    data class Success<T>(val body: T): Result<T>()
    data class Error(val code: Int? = null) : Result<Nothing>()
}