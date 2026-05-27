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
import com.photogalleryapp.ui.screens.SettingsScreen
import com.photogalleryapp.ui.screens.LicensesScreen
import com.photogalleryapp.ui.screens.SearchScreen
import com.photogalleryapp.ui.screens.AlbumContentsScreen
import com.photogalleryapp.ui.screens.FullScreenImageScreen

@Composable
fun AppNavGraph(navController: NavHostController, modifier: Modifier, mainViewModel: MainViewModel) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Album.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Album.route) { AlbumsScreen(mainViewModel, navController) }
        composable(BottomNavItem.Search.route) {
            SearchScreen(
                viewModel = mainViewModel,
                onAlbumClick = { album ->
                    navController.navigate("AlbumContents/${album.id}")
                }
            )
        }
        composable(BottomNavItem.Settings.route) { SettingsScreen(mainViewModel, navController) }
        composable("Licenses") { LicensesScreen(navController) }
        composable(
            route = "AlbumContents/{albumId}",
            arguments = listOf(navArgument("albumId") { type = NavType.IntType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getInt("albumId")
            AlbumContentsScreen(albumId, mainViewModel, navController)
        }
        composable(
            route = "FullScreenImage/{photoId}/{photoUri}",
            arguments = listOf(
                navArgument("photoId") { type = NavType.IntType },
                navArgument("photoUri") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getInt("photoId") ?: 0
            val photoUri = backStackEntry.arguments?.getString("photoUri") ?: ""
            FullScreenImageScreen(photoId, photoUri, mainViewModel, navController)
        }
    }
}
