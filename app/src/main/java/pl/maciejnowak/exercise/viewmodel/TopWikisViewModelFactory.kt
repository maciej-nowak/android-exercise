package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.maciejnowak.exercise.repository.WikiRepository

class TopWikisViewModelFactory(private val repository: WikiRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TopWikisViewModel(repository) as T
    }
}