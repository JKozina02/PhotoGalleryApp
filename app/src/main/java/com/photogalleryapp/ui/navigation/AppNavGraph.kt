package com.photogalleryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.photogalleryapp.model.MainViewModel
import com.photogalleryapp.ui.screens.AlbumsScreen
import com.photogalleryapp.ui.screens.GalleryScreen
import com.photogalleryapp.ui.screens.SearchScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier, mainViewModel: MainViewModel) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Gallery.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Gallery.route) { GalleryScreen() }
        composable(BottomNavItem.Album.route) { AlbumsScreen(mainViewModel) }
        composable(BottomNavItem.Search.route) { SearchScreen() }
    }
}