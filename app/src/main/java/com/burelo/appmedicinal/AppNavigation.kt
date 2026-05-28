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

object Routes {
    const val HOME            = "home"
    const val SEARCH_RESULTS  = "search_results/{query}"
    const val PLANTS_ACTIVITY = "plants_activity/{plantName}"
    const val VIDEO_ACTIVITY  = "video_activity/{videoUrl}"

    fun searchResults(query: String): String {
        val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        return "search_results/$encoded"
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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
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

        composable(
            route = Routes.PLANTS_ACTIVITY,
            arguments = listOf(navArgument("plantName") { type = NavType.StringType })
        ) { backStackEntry ->
            val raw = backStackEntry.arguments?.getString("plantName") ?: ""
            val plantName = URLDecoder.decode(raw, StandardCharsets.UTF_8.toString())
            PlantsActivityScreen(
                plantName = plantName,
                onBack = { navController.popBackStack() }
            )
        }

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
