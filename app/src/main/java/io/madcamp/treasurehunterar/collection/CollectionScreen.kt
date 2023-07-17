package io.madcamp.treasurehunterar.collection

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

@Composable
internal fun CollectionRoute(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    CollectionScreen(
        navController = navController,
        modifier = modifier,
    )
}

@Composable
internal fun CollectionScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    CollectionGrid(
        navController = navController,
        modifier,
    )
}

@Composable
private fun CollectionGrid(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()

    ) {items(collectionList) { collection ->
            CollectionCard(
                collection = collection,

                onCollectionCardClick = {
                    Log.d("CollectionDetail", "In Grid" + collection.collectionNum)
                    navController.navigate("collection_detail/" + "${collection.collectionNum}")}
            )
        }
    }
}

@Composable
fun CollectionCard(
    collection: Collection,
    onCollectionCardClick: (Collection) -> Unit
) {
    Box(
        modifier = Modifier.clickable(
            onClick = {
                Log.d("CollectionDetail", "Collection Card Click!" + collection.collectionNum)
                onCollectionCardClick(collection)
            }
        )
    ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = collection.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .padding(5.dp)
                        .size(50.dp)
//            contentScale = ContentScale.FillBounds
                )
                Text(
                    text = collection.name
                )
                Text(
                    text = collection.shortDescription,
                    modifier = Modifier,
                )
            }
        }
}

//@Preview
//@Composable
//fun CollectionScreenPreview() {
//    CollectionScreen(
//        modifier = Modifier,
//    )
//}

@Preview
@Composable
fun CollectionCardPreview() {
    CollectionCard(
        collection = Collection(
            collectionNum = 1,
            name = "튀김소보로",
            shortDescription = "성심당 튀김소보로",
            longDescription = "1980년 탄생 소보로, 앙금빵, 도넛의 3단 합체빵. 하나의 빵으로 3가지 맛을 즐기실 수 있는 성심당 No.1 튀김소보로",
            imageUrl = "https://www.sungsimdangmall.co.kr/data/sungsimdang/goods/sungsimdang/big/IMG01.png"
        )
    ) {}
}

var collectionList: List<Collection> = listOf(
        Collection(
            collectionNum = 1,
            name = "튀김소보로",
            shortDescription = "성심당 튀김소보로",
            longDescription = "1980년 탄생 소보로, 앙금빵, 도넛의 3단 합체빵. 하나의 빵으로 3가지 맛을 즐기실 수 있는 성심당 No.1 튀김소보로",
            imageUrl = "https://www.sungsimdangmall.co.kr/data/sungsimdang/goods/sungsimdang/big/IMG01.png"
        ),
        Collection(
            collectionNum = 2,
            name = "튀소구마",
            shortDescription = "성심당 튀소구마",
            longDescription = "튀소35주년을 기념하여 탄생한 튀소 동생. 달콤한 고구마 크림으로 속을 가득 채운 튀소구마",
            imageUrl = "https://www.sungsimdangmall.co.kr/data/sungsimdang/goods/sungsimdang/big/IMG03.png"
        ),
        Collection(
            collectionNum = 3,
            name = "초코튀소",
            shortDescription = "성심당 초코튀소",
            longDescription = "튀소 탄생 40주년을 기념하여 선보인 초코튀소는 달콤한 팥앙금을 가득 품은 바삭한 튀김소보로 위에 초콜릿 코팅을 입힌 완생의 빵 입니다.",
            imageUrl = "https://www.sungsimdangmall.co.kr/data/sungsimdang/goods/sungsimdang/big/202310454249038523284.jpg"
        ),
        Collection(
            collectionNum = 4,
            name = "쑥떡앙빵",
            shortDescription = "성심당 쑥떡앙빵",
            longDescription = "앙금빵인 줄 알았다가 한잎 베어 물면 놀라는 앙꼬인 듯 아닌 듯 찹쌀에 알밤까지..",
            imageUrl = "https://www.sungsimdangmall.co.kr/data/sungsimdang/goods/sungsimdang/big/%EC%91%A5%EB%96%A1%EC%95%99%EB%B9%B5.jpg"
        ),

)