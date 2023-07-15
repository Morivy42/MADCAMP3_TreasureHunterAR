package io.madcamp.treasurehunterar.auth

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.madcamp.treasurehunterar.navigation.Screen

@Composable
fun LoginScreen(
    navController: NavController
) {
    Button(onClick = { navController.navigate(Screen.Home.route) }) {
        Text(text = "Login")
    }
}