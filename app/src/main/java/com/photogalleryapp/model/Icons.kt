package com.photogalleryapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.graphics.vector.ImageVector

object IconRegistry{
    val icons: Map<Int, ImageVector> = mapOf(
        //Tutaj zapisujemy dostępne ikony
        0 to Icons.Default.AccountCircle,
        1 to Icons.Default.Home,
        2 to Icons.Default.Lock
    )

    fun getIconById(id: Int): ImageVector{
        return icons.getOrDefault(id, Icons.Default.AccountCircle)
    }
}