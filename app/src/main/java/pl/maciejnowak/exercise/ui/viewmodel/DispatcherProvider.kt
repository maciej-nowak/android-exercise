package pl.maciejnowak.exercise.ui.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object DispatcherProvider {
    var override: CoroutineContext? = null
    val IO: CoroutineContext
        get() = override ?: Dispatchers.IO
}