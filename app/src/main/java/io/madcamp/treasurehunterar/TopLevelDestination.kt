package io.madcamp.treasurehunterar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
    val route: String,
) {
    Home(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Default.Home,
        iconTextId = R.string.home_icon,
        titleTextId = R.string.app_name,
        route = "home",
    ),
    AR(
        selectedIcon = Icons.Filled.Face,
        unselectedIcon = Icons.Default.Face,
        iconTextId = R.string.ar_icon,
        titleTextId = R.string.app_name,
        route = "ar",
    ),
    Collection(
        selectedIcon = Icons.Filled.Place,
        unselectedIcon = Icons.Default.Place,
        iconTextId = R.string.collection,
        titleTextId = R.string.app_name,
        route = "collection",
    ),
}