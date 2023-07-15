package io.madcamp.treasurehunterar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RootNavGraph(
    navController: NavHostController
) {
    NavHost (
        navController = navController,
        startDestination = HOME_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE,
    ) {
        authNavGraph(navController)
        homeNavGraph(navController)
    }
}