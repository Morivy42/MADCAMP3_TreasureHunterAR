package io.madcamp.treasurehunterar.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import io.madcamp.treasurehunterar.R

sealed class NavBarItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
    val route: String,
) {
    object Home : NavBarItem(
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Default.Home,
            iconTextId = R.string.home_icon,
            titleTextId = R.string.app_name,
            route = "home_screen",
    )
    object AR : NavBarItem(
        selectedIcon = Icons.Filled.Face,
        unselectedIcon = Icons.Default.Face,
        iconTextId = R.string.ar_icon,
        titleTextId = R.string.app_name,
        route = "ar_screen",
    )
    object Collection: NavBarItem(
        selectedIcon = Icons.Filled.Place,
        unselectedIcon = Icons.Default.Place,
        iconTextId = R.string.collection,
        titleTextId = R.string.app_name,
        route = "collection_route",
    )
}
