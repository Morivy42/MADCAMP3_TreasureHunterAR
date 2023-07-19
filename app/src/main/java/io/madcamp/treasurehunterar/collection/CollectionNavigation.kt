package io.madcamp.treasurehunterar.collection

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val COLLECTION_ROUTE = "collection_route"
const val COLLECTION_ID_ARG = "collectionId"
const val COLLECTION_DETAIL_ROUTE = "$COLLECTION_ROUTE/{$COLLECTION_ID_ARG}"

fun NavController.navigateToCollectionDetail(collectionId: String) {
    this.navigate("$COLLECTION_ROUTE/$collectionId")
}


//fun NavGraphBuilder.collectionScreen(
//    onCollectionCardClick: (Collection) -> Unit,
//) {
//    composable(route = COLLECTION_ROUTE) {
//        CollectionRoute(onCollectionCardClick)
//    }
//}