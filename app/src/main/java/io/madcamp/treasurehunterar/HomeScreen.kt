package io.madcamp.treasurehunterar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.madcamp.treasurehunterar.auth.UserUiState
import io.madcamp.treasurehunterar.map.MapMarker
import io.madcamp.treasurehunterar.map.kaistMarkerList


@Composable
fun HomeScreen(
    navController: NavController
) {
    Column(
        Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        //ProfileCard()
        //CustomLinearProgressIndicator(0.3f)
        Text("Location")
        RegionRecommendationRow()
        Text("Hints?")
        HintCardList(navController)
    }
//    MapScreen()
}

@Composable
fun HintCardList(
    navController: NavController
) {
   // Column() {
      // HintCard(navController = navController, 0,)
       // HintCard(navController = navController, markerNum = 0,)
      //HintCard(navController = navController, markerNum = 0,)
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        itemsIndexed(kaistMarkerList) { index, it ->
            HintCard(navController = navController, markerNum = index, it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HintCard(
    navController: NavController,
    markerNum: Int,
    mapMarker: MapMarker
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            navController.navigate("map_route/$markerNum")
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = mapMarker.imageResourceId),
                contentDescription = "Hint image maybe pin?",
                modifier = Modifier.size(100.dp)
            )
            //Spacer(Modifier.width(16.dp))
            Text(text = mapMarker.snippet ?: "No Hint! :/")
        }
    }
}

@Preview
@Composable
fun RegionRecommendationRow() {
    val dataItems = listOf(
        RegionData(R.drawable.n1, "N1"),
        RegionData(R.drawable.main, "본관"),
        RegionData(R.drawable.library, "도서관"),
        RegionData(R.drawable.subway, "서브웨이"),
        RegionData(R.drawable.kaimaru, "카이마루"),
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
            items(dataItems.size) { index ->
                val dataItem = dataItems[index]
                RegionCard(data = dataItem)
                Spacer(modifier = Modifier.width(5.dp)) // Add a horizontal space of 5.dp between items
            }
    }
}

@Composable
fun RegionCard(data: RegionData) {
    Card(
        modifier = Modifier.background(Color.Transparent)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(5.dp)
        ) {
            Image(
                painter = painterResource(id = data.imageId),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
            )
            Text(text = data.regionName)
        }
    }
}

@Preview
@Composable
fun ProfileCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(20f),
    ) {
        Image (
            painter = painterResource(id = R.drawable.map),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            contentScale = ContentScale.FillBounds
        )
    }

}
//
//@Composable
//fun UsersList(
//    userUiState: UserUiState,
//    modifier: Modifier = Modifier
//) {
////    val userViewModel: UserViewModel = viewModel()
//    when (userUiState) {
//        is UserUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
//        is UserUiState.Success -> ResultScreen(
//            userUiState.Users
//        )
//        is UserUiState.Error -> ErrorScreen()
//    }
//}
//
//@Composable
//fun LoadingScreen(modifier: Modifier = Modifier) {
//    Image(
//        modifier = modifier.size(200.dp),
//        painter = painterResource(R.drawable.ic_launcher_foreground),
//        contentDescription = stringResource(R.string.app_name)
//    )
//}
//
//@Composable
//fun ErrorScreen(modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = ""
//        )
//        Text(text = "Error", modifier = Modifier.padding(16.dp))
//    }
//}
//
//@Composable
//fun ResultScreen(User: String, modifier: Modifier = Modifier) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = modifier
//    ) {
//        Text(text = User)
//    }
//}

data class RegionData(val imageId: Int, val regionName: String)

//@Composable
//fun CustomLinearProgressIndicator(
//    progress: Float,
//    modifier: Modifier = Modifier,
//    color: Color = MaterialTheme.colorScheme.primary,
//    backgroundColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
//    indicatorHeight: Dp = 10.dp,
//    indicatorPadding: Dp = 5.dp,
//) {
//    Column(
//        modifier = modifier
//            .padding(10.dp)
//            .fillMaxWidth()
//            .wrapContentHeight(),
//    ) {
//        Text(
//            text = "Collected / Total",
//            fontSize = 15.sp
//        )
//        Text(
//            text = "30%",
//            fontSize = 15.sp
//        )
//
//        Box(
//            modifier = modifier
//                .fillMaxWidth()
//                .wrapContentHeight()
//        ) {
//            Box(
//                modifier = Modifier
//                    .height(indicatorHeight)
//                    .fillMaxWidth()
//                    .background(backgroundColor)
//            )
//            Box(
//                modifier = Modifier
//                    .height(indicatorHeight)
//                    .fillMaxWidth(progress)
//                    .background(color)
//                    .padding(start = indicatorPadding, end = indicatorPadding)
//            )
//        }
//    }
//}


