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
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // deleteDatabase("gallery_db") // Usunięto, żeby nie kasować bazy przy każdym uruchomieniu, chyba że tak miało być
        setContent {
            PhotoGalleryAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Ukrywamy pasek nawigacji na ekranie logowania
                        if (currentRoute != "Login") {
                            BottomNavBar(navController)
                        }
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
