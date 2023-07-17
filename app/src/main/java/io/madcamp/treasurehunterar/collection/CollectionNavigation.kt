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


fun NavGraphBuilder.collectionDetail(
//    onBackClick: () -> Unit,
    onCollectionCardClick: (Collection) -> Unit
) {
    composable(
        route = COLLECTION_ROUTE,
        arguments = listOf(
            navArgument(COLLECTION_ID_ARG) { type = NavType.StringType}
        )
    ) {
        Log.d("CollectionDetail", "Collection Detail navGraphBuilder")
        CollectionDetail(
            collection = Collection(
                id = 1,
                name = "튀김소보로",
                shortDescription = "성심당 튀김소보로",
                longDescription = "1980년 탄생 소보로, 앙금빵, 도넛의 3단 합체빵. 하나의 빵으로 3가지 맛을 즐기실 수 있는 성심당 No.1 튀김소보로",
                imageUrl = "https://www.sungsimdangmall.co.kr/data/sungsimdang/goods/sungsimdang/big/IMG01.png"
            )
        )
    }
}

//fun NavGraphBuilder.collectionScreen(
//    onCollectionCardClick: (Collection) -> Unit,
//) {
//    composable(route = COLLECTION_ROUTE) {
//        CollectionRoute(onCollectionCardClick)
//    }
//}