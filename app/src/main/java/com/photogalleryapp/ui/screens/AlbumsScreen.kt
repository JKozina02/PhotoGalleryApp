package com.photogalleryapp.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.photogalleryapp.ui.components.albumsScreen.AlbumsGrid
import com.photogalleryapp.ui.components.albumsScreen.CreateAlbumPopup
import com.photogalleryapp.model.AlbumObject
import com.photogalleryapp.model.MainViewModel

@Composable
fun AlbumsScreen(viewModel: MainViewModel){
    var showCreateAlbumPopup by remember { mutableStateOf(false) }
    AlbumsGrid(viewModel.albums)
    Button(onClick = {showCreateAlbumPopup = true}) {
        Text("Dodaj album")
    }
    if(showCreateAlbumPopup){
        CreateAlbumPopup(
            onConfirm = {
                    albumName, albumColor, albumIcon -> viewModel.insertAlbum(
                AlbumObject(name = albumName, color = albumColor, iconID = albumIcon)
                )
                showCreateAlbumPopup = false
            },
            onDismiss = {
                showCreateAlbumPopup = false
            }
        )
    }

}