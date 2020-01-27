package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.maciejnowak.exercise.repository.ArticleRepository

class TopArticlesViewModelFactory(private val repository: ArticleRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TopArticlesViewModel(repository) as T
    }
}