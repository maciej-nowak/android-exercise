package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.withIndex
import pl.maciejnowak.exercise.ui.viewmodel.model.RefreshType
import pl.maciejnowak.repositories.ArticleRepository
import pl.maciejnowak.repositories.model.TopArticlesResult

class TopArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val refresh: MutableLiveData<RefreshType> = MutableLiveData(RefreshType.NORMAL)

    val result: LiveData<TopArticlesResult> = refresh.switchMap { force ->
        repository.fetchTopArticlesFlow(force.value).withIndex()
            .onStart { _isLoading.postValue(true) }
            .onEach { if(it.index == 0) { _isLoading.postValue(false) }}
            .map { it.value }
            .asLiveData(Dispatchers.IO)
    }

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun loadTopArticles(forceRefresh: Boolean = false) {
        refresh.value = RefreshType.valueOf(forceRefresh)
    }

    fun isSuccess(): Boolean {
        return result.value is TopArticlesResult.Success
    }
}