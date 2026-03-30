package com.photogalleryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.photogalleryapp.ui.navigation.AppNavGraph
import androidx.compose.ui.Modifier
import com.photogalleryapp.model.MainViewModel
import com.photogalleryapp.model.MainViewModelFactory
import com.photogalleryapp.ui.components.shared.BottomNavBar
import com.photogalleryapp.ui.theme.PhotoGalleryAppTheme

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        deleteDatabase("gallery_db")
        setContent {
            PhotoGalleryAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavBar(navController)
                    }
                    ){ innerPadding ->
                        AppNavGraph(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            mainViewModel = mainViewModel
                        )
                    }   
            }
        }     
    }
}
