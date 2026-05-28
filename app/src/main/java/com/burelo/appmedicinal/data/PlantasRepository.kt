package com.burelo.appmedicinal.data

import io.github.jan.supabase.postgrest.from

class PlantasRepository {

    suspend fun getPlantasByNames(names: List<String>): List<Planta> {
        return SupabaseClient.client
            .from("plantas")
            .select {
                filter {
                    isIn("nombre_comun", names)
                }
            }
            .decodeList<Planta>()
    }

    suspend fun searchPlantas(query: String): List<Planta> {
        return SupabaseClient.client
            .from("plantas")
            .select {
                filter {
                    ilike("nombre_comun", "%$query%")
                }
            }
            .decodeList<Planta>()
    }

    suspend fun getPlantaByCommonName(name: String): Planta? {
        val results = SupabaseClient.client
            .from("plantas")
            .select {
                filter {
                    eq("nombre_comun", name)
                }
                limit(1)
            }
            .decodeList<Planta>()
        return results.firstOrNull()
    }
}
