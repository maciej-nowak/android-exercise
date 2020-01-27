package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import pl.maciejnowak.exercise.ui.viewmodel.model.Result
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.ui.repository.ArticleRepository

class TopArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val retry: MutableLiveData<Boolean> = MutableLiveData(true)

    val result: LiveData<Result<List<TopArticle>>> = retry.switchMap {
        repository.fetchTopArticlesLiveData()
    }

    fun loadTopArticles() {
        retry.value = true
    }
}