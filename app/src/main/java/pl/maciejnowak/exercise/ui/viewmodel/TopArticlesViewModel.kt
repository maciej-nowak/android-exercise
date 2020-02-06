package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.withIndex
import pl.maciejnowak.exercise.ui.repository.ArticleRepository
import pl.maciejnowak.exercise.ui.viewmodel.model.TopArticlesResult

class TopArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val retry: MutableLiveData<Boolean> = MutableLiveData(true)

    val result: LiveData<TopArticlesResult> = retry.switchMap {
        repository.fetchTopArticlesFlow().withIndex()
            .onStart { _isLoading.postValue(true) }
            .onEach { if(it.index == 0) { _isLoading.postValue(false) }}
            .map { it.value }
            .asLiveData(Dispatchers.IO)
    }

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun loadTopArticles() {
        retry.value = true
    }
}