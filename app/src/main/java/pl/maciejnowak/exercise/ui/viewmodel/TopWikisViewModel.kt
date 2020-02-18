package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.maciejnowak.repositories.WikiRepository
import pl.maciejnowak.repositories.model.TopWikisResult

class TopWikisViewModel(private val repository: WikiRepository) : ViewModel() {

    init {
        loadTopWikis()
    }

    private val _result: MutableLiveData<TopWikisResult> = MutableLiveData()
    val result: LiveData<TopWikisResult>
        get() = _result

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun loadTopWikis(forceRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            val wikis = repository.fetchTopWikis(forceRefresh)
            _result.postValue(wikis)
            _isLoading.postValue(false)
        }
    }

    fun isSuccess(): Boolean {
        return result.value !is TopWikisResult.Error
    }
}