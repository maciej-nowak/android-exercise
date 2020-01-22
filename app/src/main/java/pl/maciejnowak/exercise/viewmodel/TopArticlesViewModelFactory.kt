package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.maciejnowak.exercise.interactor.TopArticlesInteractor

class TopArticlesViewModelFactory(private val interactor: TopArticlesInteractor) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TopArticlesViewModel(interactor) as T
    }
}