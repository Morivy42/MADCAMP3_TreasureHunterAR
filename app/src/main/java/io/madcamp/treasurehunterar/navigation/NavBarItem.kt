package io.madcamp.treasurehunterar.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
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
    object Map : NavBarItem(
        selectedIcon = Icons.Filled.Place,
        unselectedIcon = Icons.Default.Place,
        iconTextId = R.string.map_icon,
        titleTextId = R.string.map_icon,
        route = "ar_screen",
    )
    object Collection: NavBarItem(
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Default.Star,
        iconTextId = R.string.collection,
        titleTextId = R.string.collection,
        route = "collection_route",
    )
}
