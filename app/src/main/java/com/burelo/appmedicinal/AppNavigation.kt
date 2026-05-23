package com.burelo.appmedicinal.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// ─────────────────────────────────────────────────────────────
//  RUTAS DE NAVEGACIÓN
// ─────────────────────────────────────────────────────────────

object Routes {
    const val HOME            = "home"
    const val SEARCH_RESULTS  = "search_results/{query}"
    const val PLANT_DETAIL    = "plant_detail/{plantName}"
    const val PLANTS_ACTIVITY = "plants_activity/{plantName}"
    const val VIDEO_ACTIVITY  = "video_activity/{videoUrl}"

    fun searchResults(query: String): String {
        val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        return "search_results/$encoded"
    }
    fun plantDetail(plantName: String): String {
        val encoded = URLEncoder.encode(plantName, StandardCharsets.UTF_8.toString())
        return "plant_detail/$encoded"
    }
    fun plantsActivity(plantName: String): String {
        val encoded = URLEncoder.encode(plantName, StandardCharsets.UTF_8.toString())
        return "plants_activity/$encoded"
    }
    fun videoActivity(videoUrl: String): String {
        val encoded = URLEncoder.encode(videoUrl, StandardCharsets.UTF_8.toString())
        return "video_activity/$encoded"
    }
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
                onSearch = { query ->
                    navController.navigate(Routes.searchResults(query))
                },
                onPlantClick = { plantName ->
                    navController.navigate(Routes.plantsActivity(plantName))
                }
            )
        }

        // ── Pantalla 2: Resultados de búsqueda ─────────────
        composable(
            route = Routes.SEARCH_RESULTS,
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val raw = backStackEntry.arguments?.getString("query") ?: ""
            val query = URLDecoder.decode(raw, StandardCharsets.UTF_8.toString())
            SearchResultsScreen(
                query = query,
                onBack = { navController.popBackStack() },
                onResultClick = { plantName ->
                    navController.navigate(Routes.plantsActivity(plantName))
                }
            )
        }

        // ── Pantalla 3: Detalle de planta ───────────────────
        composable(
            route = Routes.PLANT_DETAIL,
            arguments = listOf(navArgument("plantName") { type = NavType.StringType })
        ) {
            PlantDetailScreen(
                plant = manzanillaDetail,
                onBack = { navController.popBackStack() }
            )
        }

        // ── Actividad 1: Detalle de planta medicinal ─────────
        composable(
            route = Routes.PLANTS_ACTIVITY,
            arguments = listOf(navArgument("plantName") { type = NavType.StringType })
        ) { backStackEntry ->
            val raw = backStackEntry.arguments?.getString("plantName") ?: ""
            val plantName = URLDecoder.decode(raw, StandardCharsets.UTF_8.toString())
            PlantsActivityScreen(
                plantName = plantName,
                onBack = { navController.popBackStack() },
                onVideoClick = { videoUrl ->
                    navController.navigate(Routes.videoActivity(videoUrl))
                }
            )
        }

        // ── Actividad 2: Reproductor de video YouTube ───────
        composable(
            route = Routes.VIDEO_ACTIVITY,
            arguments = listOf(navArgument("videoUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("videoUrl") ?: ""
            val videoUrl = URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
            VideoActivityScreen(
                videoUrl = videoUrl,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
