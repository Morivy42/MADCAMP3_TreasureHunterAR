package io.madcamp.treasurehunterar.collection

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import io.madcamp.treasurehunterar.ui.theme.Yellow20

@Composable
internal fun CollectionRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    CollectionScreen(
        navController = navController,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collections") },
                modifier = modifier,
            )
        }
    ) { paddingValues ->
        CollectionGrid(
            navController = navController,
            modifier.padding(paddingValues),
        )
    }
}

@Composable
private fun CollectionGrid(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.FixedSize(100.dp),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
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
        modifier = Modifier
            .size(width = 80.dp, height = 100.dp)
            .clip(shape = RoundedCornerShape(size = 12.dp))
            .shadow(5.dp)
            .background(Yellow20, RectangleShape)
//            .border(border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(size = 12.dp))
            .clickable(
                onClick = {
                    Log.d("CollectionDetail", "Collection Card Click!" + collection.collectionNum)
                    onCollectionCardClick(collection)
                }
            )
    ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
//                val colorFilter = PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
                AsyncImage(
                    model = collection.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .padding(5.dp)
                        .size(40.dp),
//                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToScale(0.3f, 0.3f, 0.3f, 0.5f) })
                )
                Text(
                    text = collection.name,
                    modifier = Modifier.sizeIn(minWidth = 70.dp),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
                Text(
                    text = collection.shortDescription,
                    modifier = Modifier.sizeIn(minWidth = 80.dp),
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
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