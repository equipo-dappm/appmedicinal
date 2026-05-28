package com.burelo.appmedicinal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burelo.appmedicinal.data.Planta
import com.burelo.appmedicinal.data.PlantasRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : ViewModel() {
    private val repository = PlantasRepository()

    var results by mutableStateOf<List<Planta>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var query by mutableStateOf("")
        private set

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        query = newQuery
        searchJob?.cancel()
        if (newQuery.isBlank()) {
            results = emptyList()
            return
        }
        searchJob = viewModelScope.launch {
            delay(300)
            isLoading = true
            withContext(Dispatchers.IO) {
                try {
                    results = repository.searchPlantas(newQuery)
                } catch (_: Exception) {
                    results = emptyList()
                }
            }
            isLoading = false
        }
    }
}
