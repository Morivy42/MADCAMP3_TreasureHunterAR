package io.madcamp.treasurehunterar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.madcamp.treasurehunterar.HomeScreen
import io.madcamp.treasurehunterar.collection.navigateToCollectionDetail

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Home.route,
        route = HOME_GRAPH_ROUTE,
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen()
        }
//        collectionDetail {
//            navController.navigateToCollectionDetail("1")
//        }
    }
}