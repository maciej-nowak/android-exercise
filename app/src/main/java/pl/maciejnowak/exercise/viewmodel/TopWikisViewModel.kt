package pl.maciejnowak.exercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.maciejnowak.exercise.interactor.TopWikisInteractor
import pl.maciejnowak.exercise.model.TopWiki

class TopWikisViewModel(private val interactor: TopWikisInteractor) : ViewModel() {

    private val items = MutableLiveData<List<TopWiki>>()
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
            val response = withContext(Dispatchers.IO) {
                delay(3000) //mock some delay
                interactor.fetch()
            }
            isLoading.value = false
            if(response != null) {
                items.value = response
                error.value = false
            } else {
                error.value = true
            }
        }
    }
}