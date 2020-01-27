package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import pl.maciejnowak.exercise.Resource
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.repository.ArticleRepository

class TopArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val retry: MutableLiveData<Boolean> = MutableLiveData(true)

    val result: LiveData<Resource<List<TopArticle>>> = retry.switchMap {
        repository.fetchTopArticlesLiveData()
    }

    fun loadTopArticles() {
        retry.value = true
    }
}