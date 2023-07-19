package io.madcamp.treasurehunterar

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.madcamp.treasurehunterar.auth.UserViewModel
import io.madcamp.treasurehunterar.theme.TreasureHunterARTheme
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import io.madcamp.treasurehunterar.treasure.CloudAnchorActivity

class MainActivity : ComponentActivity() {
    private val userViewModel by viewModels<UserViewModel>()

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
}


