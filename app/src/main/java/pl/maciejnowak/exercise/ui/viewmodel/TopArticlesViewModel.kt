package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import pl.maciejnowak.exercise.ui.viewmodel.model.Result
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.ui.repository.ArticleRepository

class TopArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val retry: MutableLiveData<Boolean> = MutableLiveData(true)

    val result: LiveData<Result<List<TopArticle>>> = retry.switchMap {
        repository.fetchTopArticlesFlow().asLiveData(Dispatchers.IO)
    }

    fun loadTopArticles() {
        retry.value = true
    }
}