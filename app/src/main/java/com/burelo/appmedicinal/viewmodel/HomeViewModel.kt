package com.burelo.appmedicinal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burelo.appmedicinal.data.Planta
import com.burelo.appmedicinal.data.PlantasRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private val repository = PlantasRepository()

    var plantas by mutableStateOf<List<Planta>>(emptyList())
        private set
    var isLoading by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    private val featuredNames = listOf(
        "Maguey morado",
        "Guácimo",
        "Sábila",
        "Epazote"
    )

    init {
        loadFeaturedPlantas()
    }

    fun loadFeaturedPlantas() {
        viewModelScope.launch {
            isLoading = true
            error = null
            withContext(Dispatchers.IO) {
                try {
                    val result = repository.getPlantasByNames(featuredNames)
                    plantas = result
                } catch (e: Exception) {
                    error = e.message ?: "Error al cargar plantas"
                }
            }
            isLoading = false
        }
    }
}
