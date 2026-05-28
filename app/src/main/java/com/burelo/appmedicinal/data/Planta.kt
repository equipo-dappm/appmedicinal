package com.burelo.appmedicinal.data

import kotlinx.serialization.Serializable

@Serializable
data class Planta(
    val id: Long,
    val nombre_cientifico: String,
    val nombre_comun: String,
    val origen: String? = null,
    val manejo: String? = null,
    val forma_de_vida: String? = null,
    val imagen_url: String? = null,
    val descripcion_uso: String? = null,
)
