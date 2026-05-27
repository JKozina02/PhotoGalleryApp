package com.photogalleryapp.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.photogalleryapp.R

enum class BottomNavItem (
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val route: String
) {
    Album(R.string.albums_tab, Icons.Default.AccountBox, "Albums"),
    Search(R.string.search_tab, Icons.Default.Search, "Search"),
    Settings(R.string.settings, Icons.Default.Settings, "Settings")
}