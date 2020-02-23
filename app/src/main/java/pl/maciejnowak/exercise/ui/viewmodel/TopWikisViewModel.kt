package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pl.maciejnowak.commonobjects.OpenForTesting
import pl.maciejnowak.repositories.WikiRepository
import pl.maciejnowak.repositories.model.TopWikisResult

@OpenForTesting
class TopWikisViewModel(
    private val repository: WikiRepository,
    private val dispatcher: DispatcherProvider = DefaultDispatcherProvider(),
    init: Boolean = true
) : ViewModel() {

    private val _result: MutableLiveData<TopWikisResult> = MutableLiveData()
    val result: LiveData<TopWikisResult>
        get() = _result

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        if(init) this.loadTopWikis()
    }

    fun loadTopWikis(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher.io()) {
            _isLoading.postValue(true)
            val wikis = repository.fetchTopWikis(forceRefresh)
            _result.postValue(wikis)
            _isLoading.postValue(false)
        }
    }

    fun isSuccess(): Boolean {
        return result.value !is TopWikisResult.Error && result.value != null
    }
}