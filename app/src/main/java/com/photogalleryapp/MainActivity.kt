package com.photogalleryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.photogalleryapp.ui.navigation.AppNavGraph
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.photogalleryapp.composable.screens.AlbumsScreen
import com.photogalleryapp.model.MainViewModel
import com.photogalleryapp.model.MainViewModelFactory
import com.photogalleryapp.ui.theme.PhotoGalleryAppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(applicationContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoGalleryAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        AlbumsScreen(viewModel)
                        AppNavGraph(navController)
                    }

                }
            }
        }
    }
}
