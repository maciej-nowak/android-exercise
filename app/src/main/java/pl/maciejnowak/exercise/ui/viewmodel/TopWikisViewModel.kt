package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.withIndex
import pl.maciejnowak.exercise.ui.repository.WikiRepository
import pl.maciejnowak.exercise.ui.viewmodel.model.TopWikisResult

class TopWikisViewModel(private val repository: WikiRepository) : ViewModel() {

    private val refresh: MutableLiveData<Boolean> = MutableLiveData(false)

    val result: LiveData<TopWikisResult> = refresh.switchMap { force ->
        repository.fetchTopWikisFlow(force).withIndex()
            .onStart { _isLoading.postValue(true) }
            .onEach { if(it.index == 0) { _isLoading.postValue(false) }}
            .map { it.value }
            .asLiveData(Dispatchers.IO)
    }

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun loadTopWikis(forceRefresh: Boolean = false) {
        refresh.value = forceRefresh
    }
}