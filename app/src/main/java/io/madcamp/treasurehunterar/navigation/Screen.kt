package io.madcamp.treasurehunterar.navigation

sealed class Screen(val route: String) {
    object Main: Screen(route = "main_screen")
    object Home: Screen(route = "home_screen")
    object AR: Screen(route = "ar_screen")
    object Collection: Screen(route = "collection_screen?collectionId={collectionId}") {
        fun passcollectionId(
            collectionId: Int = 0,
        ): String {
            return "collection_screen?id=$collectionId"
        }
    }
    object Login: Screen(route = "login_screen")
    object SignUp: Screen(route = "signup_screen")
}

const val COLLECTION_ID = "collectionId"

const val ROOT_GRAPH_ROUTE = "root"
const val AUTH_GRAPH_ROUTE = "auth"
const val HOME_GRAPH_ROUTE = "home"
const val MAIN_GRAPH_ROUTE = "main"