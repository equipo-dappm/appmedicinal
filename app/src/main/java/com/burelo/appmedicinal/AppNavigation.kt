package com.burelo.appmedicinal.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// ─────────────────────────────────────────────────────────────
//  RUTAS DE NAVEGACIÓN
// ─────────────────────────────────────────────────────────────

object Routes {
    const val HOME           = "home"
    const val SEARCH_RESULTS = "search_results/{query}"
    const val PLANT_DETAIL   = "plant_detail/{plantName}"

    fun searchResults(query: String) = "search_results/$query"
    fun plantDetail(plantName: String) = "plant_detail/$plantName"
}

// ─────────────────────────────────────────────────────────────
//  NAV HOST PRINCIPAL
// ─────────────────────────────────────────────────────────────

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        // ── Pantalla 1: Inicio ──────────────────────────────
        composable(Routes.HOME) {
            NaturaMedScreen(
                // Cuando el usuario busca desde el SearchBar
                onSearch = { query ->
                    navController.navigate(Routes.searchResults(query))
                },
                // Cuando toca una tarjeta de planta
                onPlantClick = { plantName ->
                    navController.navigate(Routes.plantDetail(plantName))
                }
            )
        }

        // ── Pantalla 2: Resultados de búsqueda ─────────────
        composable(
            route = Routes.SEARCH_RESULTS,
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchResultsScreen(
                query = query,
                onBack = { navController.popBackStack() },
                onResultClick = { plantName ->
                    navController.navigate(Routes.plantDetail(plantName))
                }
            )
        }

        // ── Pantalla 3: Detalle de planta ───────────────────
        composable(
            route = Routes.PLANT_DETAIL,
            arguments = listOf(navArgument("plantName") { type = NavType.StringType })
        ) {
            // Aquí podrías buscar la planta por nombre en tu ViewModel/repositorio
            PlantDetailScreen(
                plant = manzanillaDetail,   // reemplaza con búsqueda real por nombre
                onBack = { navController.popBackStack() }
            )
        }
    }
}
