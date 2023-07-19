package io.madcamp.treasurehunterar.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import io.madcamp.treasurehunterar.R

sealed class NavBarItem(
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val route: String,
) {
    object Home : NavBarItem(
            unselectedIcon = Icons.Default.Home,
            iconTextId = R.string.home_icon,
            route = "home_screen",
    )
    object Map : NavBarItem(
        unselectedIcon = Icons.Default.Place,
        iconTextId = R.string.map_icon,
        route = "ar_screen",
    )
    object Collection: NavBarItem(
        unselectedIcon = Icons.Default.Star,
        iconTextId = R.string.collection,
        route = "collection_route",
    )
}
