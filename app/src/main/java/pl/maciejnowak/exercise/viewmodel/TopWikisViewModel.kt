package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import pl.maciejnowak.exercise.Resource
import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.repository.WikiRepository

class TopWikisViewModel(private val repository: WikiRepository) : ViewModel() {

    val result: LiveData<Resource<List<TopWiki>>> = repository.resource
}