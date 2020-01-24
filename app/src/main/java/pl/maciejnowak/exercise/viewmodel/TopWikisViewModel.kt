package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.maciejnowak.exercise.database.model.TopWiki
import pl.maciejnowak.exercise.repository.WikiRepository

class TopWikisViewModel(private val repository: WikiRepository) : ViewModel() {

    private val items = repository.items
    private val isLoading = MutableLiveData<Boolean>()
    private val error = MutableLiveData<Boolean>()

    fun getItems(): LiveData<List<TopWiki>> {
        return items
    }

    fun getIsLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getError(): LiveData<Boolean> {
        return error
    }

    fun loadTopWikis() {
        viewModelScope.launch {
            isLoading.value = true
            withContext(Dispatchers.IO) { repository.reloadTopWikis() }
            isLoading.value = false
        }
    }
}