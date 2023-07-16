package io.madcamp.treasurehunterar.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import io.madcamp.treasurehunterar.R

@Composable
fun CollectionDetail(
    navController: NavController,

){
    Column() {
        Text(text="Collection Title")
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Sample Collection Image"
        )
        Text(text = "Collection Description")
    }
}