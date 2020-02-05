package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import pl.maciejnowak.exercise.ui.viewmodel.model.Result
import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.ui.repository.WikiRepository

class TopWikisViewModel(private val repository: WikiRepository) : ViewModel() {

    private val retry: MutableLiveData<Boolean> = MutableLiveData(true)

    val result: LiveData<Result<List<TopWiki>>> = retry.switchMap {
        repository.fetchTopWikisFlow().asLiveData(Dispatchers.IO)
    }

    fun loadTopWikis() {
        retry.value = true
    }
}