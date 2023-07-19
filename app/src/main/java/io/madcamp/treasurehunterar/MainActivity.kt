package io.madcamp.treasurehunterar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.madcamp.treasurehunterar.auth.UserViewModel
import io.madcamp.treasurehunterar.theme.TreasureHunterARTheme
import io.madcamp.treasurehunterar.treasure.CloudAnchorActivity

class MainActivity : ComponentActivity() {
    private val userViewModel by viewModels<UserViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeDataInFirebase()
        setViewContent()
    }

    private fun setViewContent() {
        setContent {
            TreasureHunterARTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TreasureHunterARApp()
//                RootNavGraph(navController = rememberNavController())
                    Box {
                        HorizontalDraggableSample(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    bottom = 100.dp
                                ),
                            onFABClick = { startJavaActivity() }
                        )
                    }
                }
            }
        }
    }
    private fun startJavaActivity() {
        val intent = Intent(this, CloudAnchorActivity::class.java)
        startActivity(intent)
    }

    override fun onResume(){
        super.onResume()
        Log.d("test", "1")
    }

    private fun storeDataInFirebase() {
        val db = FirebaseApp.getInstance()

        if (db != null) {
            // Get a reference to the FirebaseDatabase
            val rootRef: DatabaseReference = FirebaseDatabase.getInstance(db).reference
            DatabaseReference.goOnline()

            for (collection in collectionList) {
                val data: MutableMap<String, Any> = HashMap()
                data["collectionNum"] = collection.collectionNum
                data["name"] = collection.name
                data["shortDescription"] = collection.shortDescription
                data["longDescription"] = collection.longDescription
                data["imageUrl"] = collection.imageUrl
                data["isFound"] = false

                // Get a reference to the specific document under the "collection" collection using the collectionNum as the key
                val collectionRef: DatabaseReference = rootRef.child("collection").child(collection.collectionNum.toString())
                // Set the data for the document
                collectionRef.setValue(data)
                    .addOnSuccessListener { /* Do something on success if needed */ }
                    .addOnFailureListener { e -> /* Handle failure if needed */ }
            }
        }
    }

    val collectionList = listOf(
        io.madcamp.treasurehunterar.collection.Collection(
            collectionNum = 1,
            name = "튀김소보로",
            shortDescription = "성심당 튀김소보로",
            longDescription = "1980년 탄생 소보로, 앙금빵, 도넛의 3단 합체빵. 하나의 빵으로 3가지 맛을 즐기실 수 있는 성심당 No.1 튀김소보로",
            imageUrl = "https://www.sungsimdangmall.co.kr/data/sungsimdang/goods/sungsimdang/big/IMG01.png",
            isFound = false
        ),
        io.madcamp.treasurehunterar.collection.Collection(
            collectionNum = 2,
            name = "튀소구마",
            shortDescription = "성심당 튀소구마",
            longDescription = "튀소35주년을 기념하여 탄생한 튀소 동생. 달콤한 고구마 크림으로 속을 가득 채운 튀소구마",
            imageUrl = "https://www.sungsimdangmall.co.kr/data/sungsimdang/goods/sungsimdang/big/IMG03.png",
            isFound = false
        ),
        io.madcamp.treasurehunterar.collection.Collection(
            collectionNum = 3,
            name = "초코튀소",
            shortDescription = "성심당 초코튀소",
            longDescription = "튀소 탄생 40주년을 기념하여 선보인 초코튀소는 달콤한 팥앙금을 가득 품은 바삭한 튀김소보로 위에 초콜릿 코팅을 입힌 완생의 빵 입니다.",
            imageUrl = "https://www.sungsimdangmall.co.kr/data/sungsimdang/goods/sungsimdang/big/202310454249038523284.jpg",
            isFound = false
        ),
        io.madcamp.treasurehunterar.collection.Collection(
            collectionNum = 4,
            name = "쑥떡앙빵",
            shortDescription = "성심당 쑥떡앙빵",
            longDescription = "앙금빵인 줄 알았다가 한잎 베어 물면 놀라는 앙꼬인 듯 아닌 듯 찹쌀에 알밤까지..",
            imageUrl = "https://www.sungsimdangmall.co.kr/data/sungsimdang/goods/sungsimdang/big/%EC%91%A5%EB%96%A1%EC%95%99%EB%B9%B5.jpg",
            isFound = false
        ),
    )
}


