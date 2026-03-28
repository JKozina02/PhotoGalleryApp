package com.photogalleryapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem (
    val label: String,
    val icon: ImageVector,
    val route: String
) {
    Gallery("Galeria", Icons.Default.Photo, "Gallery"),
    Album("Albumy", Icons.Default.PhotoAlbum, "Albums"),
    Search("Szukaj", Icons.Default.Search, "Search")
}