package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.maciejnowak.exercise.interactor.TopWikisInteractor

class TopArticlesViewModelFactory(private val interactor: TopWikisInteractor) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TopWikisViewModel(interactor) as T
    }
}