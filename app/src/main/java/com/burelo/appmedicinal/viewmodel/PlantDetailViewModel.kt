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

class PlantDetailViewModel : ViewModel() {
    private val repository = PlantasRepository()

    var planta by mutableStateOf<Planta?>(null)
        private set
    var isLoading by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun loadPlanta(name: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            withContext(Dispatchers.IO) {
                try {
                    planta = repository.getPlantaByCommonName(name)
                } catch (e: Exception) {
                    error = e.message ?: "Error al cargar la planta"
                }
            }
            isLoading = false
        }
    }
}
