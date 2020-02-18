package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pl.maciejnowak.repositories.ArticleRepository
import pl.maciejnowak.repositories.model.TopArticlesResult

class TopArticlesViewModel(
    private val repository: ArticleRepository,
    private val dispatcher: DispatcherProvider = DispatcherProvider
) : ViewModel() {

    init {
        loadTopArticles()
    }

    private val _result: MutableLiveData<TopArticlesResult> = MutableLiveData()
    val result: LiveData<TopArticlesResult>
        get() = _result

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun loadTopArticles(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher.IO) {
            _isLoading.postValue(true)
            val articles = repository.fetchTopArticles(forceRefresh)
            _result.postValue(articles)
            _isLoading.postValue(false)
        }
    }

    fun isSuccess(): Boolean {
        return result.value !is TopArticlesResult.Error
    }
}