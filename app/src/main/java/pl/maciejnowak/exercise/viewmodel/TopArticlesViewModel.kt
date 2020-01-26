package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import pl.maciejnowak.exercise.Resource
import pl.maciejnowak.exercise.database.model.TopArticle
import pl.maciejnowak.exercise.repository.ArticleRepository

class TopArticlesViewModel(private val repository: ArticleRepository) : ViewModel() {

    val result: LiveData<Resource<List<TopArticle>>> = repository.resource
}