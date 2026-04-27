package com.photogalleryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.photogalleryapp.model.MainViewModel
import com.photogalleryapp.ui.screens.AlbumsScreen
import com.photogalleryapp.ui.screens.GalleryScreen
import com.photogalleryapp.ui.screens.SearchScreen
import com.photogalleryapp.ui.screens.AlbumContentsScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier, mainViewModel: MainViewModel) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Gallery.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Gallery.route) { GalleryScreen() }
        composable(BottomNavItem.Album.route) { AlbumsScreen(mainViewModel, navController) }
        composable(BottomNavItem.Search.route) { SearchScreen() }
        composable(
            route = "AlbumContents/{albumId}",
            arguments = listOf(navArgument("albumId") { type = NavType.IntType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getInt("albumId")
            AlbumContentsScreen(albumId, mainViewModel, navController)
        }
    }
}
