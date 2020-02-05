package pl.maciejnowak.exercise.ui.viewmodel.model

sealed class Result<out T>(val data: T? = null) {

    class Success<T>(data: T): Result<T>(data)
    class Error<T>(val type: ErrorType? = null, data: T? = null): Result<T>(data)
}