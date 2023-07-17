package io.madcamp.treasurehunterar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import io.madcamp.treasurehunterar.AR.ColorViewModel
import io.madcamp.treasurehunterar.auth.UserViewModel
import io.madcamp.treasurehunterar.ui.theme.TreasureHunterARTheme

class MainActivity : ComponentActivity() {
    private val userViewModel by viewModels<UserViewModel>()
    private val colorViewModel by viewModels<ColorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    TreasureHunterARApp(colorViewModel)
//                RootNavGraph(navController = rememberNavController())
                }
            }
        }
    }
}


// original onCreated
//override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContent {
//        TreasureHunterARTheme {
//            // A surface container using the 'background' color from the theme
//            Surface(
//                modifier = Modifier.fillMaxSize(),
//                color = MaterialTheme.colorScheme.background
//            ) {
//                TreasureHunterARApp()
//            }
//        }
//    }
//}