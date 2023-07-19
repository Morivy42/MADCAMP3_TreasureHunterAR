package io.madcamp.treasurehunterar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ProfileCard()
        CustomLinearProgressIndicator(0.3f)
        RegionRecommendationRow()
        HintCardList(navController)
    }
//    MapScreen()
}

@Composable
fun HintCardList(
    navController: NavController
) {
//    Column() {
//        HintCard(navController = navController, markerNum = 0)
//        HintCard(navController = navController, markerNum = 0)
//        HintCard(navController = navController, markerNum = 0)
//
//    }
    LazyColumn() {
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
        Row() {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Hint image maybe pin?"
            )
            Text(text = mapMarker.snippet ?: "No Hint! :/")
        }
    }
}


@Composable
fun RegionRecommendationRow() {

//    LazyRow(
//
//    ) {
//
//    }
    Row() {
        RegionCard()
        RegionCard()
        RegionCard()
        RegionCard()
        RegionCard()
        RegionCard()
    }
}

@Preview
@Composable
fun RegionCard() {
    Card(
        modifier = Modifier.background(Color.Transparent)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Blue)
        )
        Text(text = "region")
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
        Text(
            text = "Welcome, " + "name",
            fontSize = 32.sp
        )
    }

}

@Composable
fun CustomLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
    indicatorHeight: Dp = 10.dp,
    indicatorPadding: Dp = 5.dp,
) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Text(
            text = "Collected / Total",
            fontSize = 15.sp
        )
        Text(
            text = "30%",
            fontSize = 15.sp
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Box(
                modifier = Modifier
                    .height(indicatorHeight)
                    .fillMaxWidth()
                    .background(backgroundColor)
            )
            Box(
                modifier = Modifier
                    .height(indicatorHeight)
                    .fillMaxWidth(progress)
                    .background(color)
                    .padding(start = indicatorPadding, end = indicatorPadding)
            )
        }
    }
}



@Composable
fun UsersList(
    userUiState: UserUiState,
    modifier: Modifier = Modifier
) {
//    val userViewModel: UserViewModel = viewModel()
    when (userUiState) {
        is UserUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is UserUiState.Success -> ResultScreen(
            userUiState.Users
        )
        is UserUiState.Error -> ErrorScreen()
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = stringResource(R.string.app_name)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = ""
        )
        Text(text = "Error", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ResultScreen(User: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(text = User)
    }
}

