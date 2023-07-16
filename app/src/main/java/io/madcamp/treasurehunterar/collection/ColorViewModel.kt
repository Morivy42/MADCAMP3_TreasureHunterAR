package io.madcamp.treasurehunterar.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.madcamp.treasurehunterar.AR.MeshColor

class ColorViewModel : ViewModel() {

    var currentColor by mutableStateOf(MeshColor(-1, -1, 0))
        private set

    fun setCurrentColorInt(color: MeshColor) {
        val diff = color.timestamp - currentColor.timestamp
        if (diff > 100000000) {
            currentColor = color
        }
    }
}