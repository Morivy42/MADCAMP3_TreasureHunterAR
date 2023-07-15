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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.madcamp.treasurehunterar.AR.ARScreen
import io.madcamp.treasurehunterar.AR.ColorViewModel
import io.madcamp.treasurehunterar.collection.CollectionDetail
import io.madcamp.treasurehunterar.collection.CollectionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreasureHunterARApp(colorViewModel: ColorViewModel) {
    val navController = rememberNavController()
    val destinations = listOf(
        TopLevelDestination.Home,
        TopLevelDestination.AR,
        TopLevelDestination.Collection,
    )
    Scaffold(
        bottomBar = {
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
        NavHost(navController, startDestination = TopLevelDestination.Home.route, Modifier.padding(innerPadding)) {
            composable( TopLevelDestination.Home.route ) { HomeScreen() }
            composable( TopLevelDestination.AR.route ) { ARScreen(colorViewModel) }
            composable( TopLevelDestination.Collection.route ) { CollectionScreen(navController) }
            composable(
                route = "collection/{collectionId}",
                arguments = listOf(
                    navArgument("collectionId") {
                        type = NavType.StringType
                    }
                )
            ) {
                val collectionId = it.arguments?.getString("collectionId")!!

                CollectionDetail(
                    navController
                )
            }
        }
    }
}




//@Composable
//private fun TharBottomBar (
//    destinations: List<TopLevelDestination>,
//    onNavigateToDestination: (TopLevelDestination) -> Unit,
//    currentDestination: NavDestination?,
//    modifier: Modifier = Modifier,
//) {
//    NavigationBar {
//        destinations.forEach {destination ->
//            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
//            NavigationBarItem(
//                selected = selected,
//                onClick = { onNavigateToDestination(destination) },
//                icon = {
//                    Icon(
//                        imageVector = destination.unselectedIcon,
//                        contentDescription = null
//                    )
//                },
////                selectedIcon = {
////                    Icon(
////                        imageVector = destination.unselectedIcon,
////                        contentDescription = null
////                    )
////                },
//                label = { Text(stringResource(id = destination.iconTextId)) },
//                modifier = Modifier,
//            )
//        }
//    }
//}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
