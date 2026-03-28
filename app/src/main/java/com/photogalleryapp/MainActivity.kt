package com.photogalleryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.photogalleryapp.ui.components.BottomNavBar
import com.photogalleryapp.ui.navigation.AppNavGraph
import com.photogalleryapp.ui.theme.PhotoGalleryAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoGalleryAppTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { paddingValues ->
                    AppNavGraph(navController)
                }
            }
        }
    }
}
