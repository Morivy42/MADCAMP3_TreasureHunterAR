package io.madcamp.treasurehunterar.collection

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import io.madcamp.treasurehunterar.R

@Composable
fun CollectionScreen(
    navController: NavController
) {

    // Region
    // collections
    LazyRow {
       items(collectionList) { collection: Collection ->
            CollectionCard(
                navController = navController,
                collection = collection
            )
        }
   }
}

@Composable
fun CollectionCard(
    navController: NavController,
    collection: Collection
) {
    Column(
        modifier = Modifier.clickable(onClick = {
            Log.d("Collection", "CollectionCard Clicked")
            navController.navigate("collection/{collectionId}")
            }
        )
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Sample Collection Image"
        )
        Text(
            text = collection.name
        )
        Text(
            text = collection.shortDescription
        )
    }
}

val collectionList: List<Collection> = listOf(
    Collection(id = "1", name = "컬렉션 1", shortDescription = "1번 컬렉션", longDescription = "1번 컬렉션입니다. 1번 컬렉션입니다. "),
    Collection(id = "2", name = "컬렉션 2", shortDescription = "2번 컬렉션", longDescription = "2번 컬렉션입니다. 2번 컬렉션입니다. "),
    Collection(id = "3", name = "컬렉션 3", shortDescription = "3번 컬렉션", longDescription = "3번 컬렉션입니다. 3번 컬렉션입니다. "),
    Collection(id = "4", name = "컬렉션 4", shortDescription = "4번 컬렉션", longDescription = "4번 컬렉션입니다. 4번 컬렉션입니다. "),
    Collection(id = "5", name = "컬렉션 5", shortDescription = "5번 컬렉션", longDescription = "5번 컬렉션입니다. 5번 컬렉션입니다. "),
    Collection(id = "6", name = "컬렉션 6", shortDescription = "6번 컬렉션", longDescription = "6번 컬렉션입니다. 6번 컬렉션입니다. "),

)