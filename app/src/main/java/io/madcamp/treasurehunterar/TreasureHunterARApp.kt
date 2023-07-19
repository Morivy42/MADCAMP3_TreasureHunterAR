package io.madcamp.treasurehunterar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.madcamp.treasurehunterar.collection.CollectionDetail
import io.madcamp.treasurehunterar.collection.CollectionRoute
import io.madcamp.treasurehunterar.collection.CollectionViewModel
import io.madcamp.treasurehunterar.map.MapScreen
import io.madcamp.treasurehunterar.navigation.NavBarItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreasureHunterARApp() {
    val navController = rememberNavController()
    val collectionViewModel: CollectionViewModel = viewModel()
    Scaffold(
        bottomBar = {
            val destinations = listOf(
                NavBarItem.Home,
                NavBarItem.Map,
                NavBarItem.Collection,
            )
            NavigationBar() {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                destinations.forEach { destination ->
                    val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = destination.unselectedIcon,
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(id = destination.iconTextId)) },
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = NavBarItem.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(NavBarItem.Home.route) {
                HomeScreen()
            }
            composable(NavBarItem.Map.route) {
                MapScreen()
            }
            composable(NavBarItem.Collection.route) {
                CollectionRoute(
                    navController,
                    Modifier,
                    collectionViewModel
                )
            }
            composable(
                "collection_detail/{collectionId}",
                arguments = listOf(
                    navArgument("collectionId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                CollectionDetail(
                    collectionId = backStackEntry.arguments?.getInt("collectionId")!! - 1,
                    collectionViewModel
                )
            }
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: NavBarItem) =
    this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false
