package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import pl.maciejnowak.repositories.ArticleRepository
import pl.maciejnowak.repositories.model.TopArticlesResult

class TopArticlesViewModel(
    private val repository: ArticleRepository,
    private val dispatcher: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    private val _result: MutableLiveData<TopArticlesResult> = MutableLiveData()
    val result: LiveData<TopArticlesResult>
        get() = _result

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        loadTopArticles()
    }

    fun loadTopArticles(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatcher.io()) {
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