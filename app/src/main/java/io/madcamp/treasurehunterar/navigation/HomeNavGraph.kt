package io.madcamp.treasurehunterar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import io.madcamp.treasurehunterar.AR.ARScreen
import io.madcamp.treasurehunterar.AR.ColorViewModel
import io.madcamp.treasurehunterar.HomeScreen
import io.madcamp.treasurehunterar.TreasureHunterARApp
import io.madcamp.treasurehunterar.collection.CollectionDetail

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Main.route,
        route = MAIN_GRAPH_ROUTE,
    ) {
        composable(
            route = Screen.Main.route
        ) {
            TreasureHunterARApp(colorViewModel = ColorViewModel())
        }
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen()
        }
        composable(
            route = Screen.AR.route
        ) {
            ARScreen(colorViewModel = ColorViewModel())
        }
        composable(
            route = Screen.Collection.route,
            arguments = listOf(
                navArgument(COLLECTION_ID) {
                    type = NavType.IntType
                    defaultValue = 0
                },
            )
        ) {
            CollectionDetail(navController)
        }
    }
}