package io.madcamp.treasurehunterar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun CollectionDetail(

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