package pl.maciejnowak.exercise.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import pl.maciejnowak.exercise.ui.viewmodel.model.Result
import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.ui.repository.WikiRepository

class TopWikisViewModel(private val repository: WikiRepository) : ViewModel() {

    private val retry: MutableLiveData<Boolean> = MutableLiveData(true)

    val result: LiveData<Result<List<TopWiki>>> = retry.switchMap {
        repository.fetchTopWikisLiveData()
    }

    fun loadTopWikis() {
        retry.value = true
    }
}