package com.photogalleryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.photogalleryapp.ui.screens.AlbumsScreen
import com.photogalleryapp.ui.screens.GalleryScreen
import com.photogalleryapp.ui.screens.SearchScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Gallery.route
    ) {
        composable(BottomNavItem.Gallery.route) { GalleryScreen() }
        composable(BottomNavItem.Album.route) { AlbumsScreen() }
        composable(BottomNavItem.Search.route) { SearchScreen() }
    }
}