package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.maciejnowak.exercise.interactor.TopArticlesInteractor
import pl.maciejnowak.exercise.model.TopArticle

class TopArticlesViewModel(private val interactor: TopArticlesInteractor) : ViewModel() {

    private val items = MutableLiveData<List<TopArticle>>()
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
            val response = withContext(Dispatchers.IO) {
                delay(3000) //mock some delay
                interactor.fetch()
            }
            isLoading.value = false
            if(response != null) {
                items.value = response
                error.value = false
            } else {
                error.value = true
            }
        }
    }
}