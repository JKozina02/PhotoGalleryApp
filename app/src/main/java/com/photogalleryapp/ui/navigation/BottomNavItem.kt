package com.photogalleryapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem (
    val label: String,
    val icon: ImageVector,
    val route: String
) {
    Gallery("Galeria", Icons.Default.Home, "Gallery"),
    Album("Albumy", Icons.Default.AccountBox, "Albums"),
    Search("Szukaj", Icons.Default.Search, "Search")
}