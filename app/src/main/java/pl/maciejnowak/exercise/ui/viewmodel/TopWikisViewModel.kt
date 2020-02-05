package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import pl.maciejnowak.exercise.ui.repository.WikiRepository
import pl.maciejnowak.exercise.ui.viewmodel.model.TopWikisResult

class TopWikisViewModel(private val repository: WikiRepository) : ViewModel() {

    private val retry: MutableLiveData<Boolean> = MutableLiveData(true)

    val result: LiveData<TopWikisResult> = retry.switchMap {
        repository.fetchTopWikisFlow().asLiveData(Dispatchers.IO)
    }

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun loadTopWikis() {
        _isLoading.value = true
        retry.value = true
    }
}