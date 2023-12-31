package io.madcamp.treasurehunterar.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CollectionDetail(
    collectionId : Int,
    collectionViewModel: CollectionViewModel
){
    val collectionList =
        when (val collectionUiState = collectionViewModel.collectionUiState) {
            is CollectionUiState.Success -> collectionUiState.Collections
            is CollectionUiState.Loading -> emptyList()
            is CollectionUiState.Error -> emptyList()
        }
    val collection = collectionList[collectionId]
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow)
            .padding(50.dp)
    ) {
        Surface(
            tonalElevation = 30.dp,
            shadowElevation = 50.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
//                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text= "# " + collection.collectionNum + "\t" + collection.name,
                        modifier = Modifier,
                    )
                }
                AsyncImage(
                    model = collection.imageUrl,
                    contentDescription = "Translated description of what the image contains",
                    modifier = Modifier.size(300.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text= collection.longDescription,
                        modifier = Modifier,
                    )
                }
            }
        }
    }

}
