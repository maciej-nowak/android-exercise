package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.repository.ArticleRepository

class TopArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val items = repository.items
    private val isLoading = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Boolean>()

    fun getItems(): LiveData<List<TopArticle>> {
        return items
    }

    fun getIsLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getError(): LiveData<Boolean> {
        return error
    }

    fun loadTopArticles() {
        viewModelScope.launch {
            isLoading.value = true
            withContext(Dispatchers.IO) { repository.reloadTopArticles() }
            isLoading.value = false
        }
    }
}