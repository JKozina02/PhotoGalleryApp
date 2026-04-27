package com.photogalleryapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.photogalleryapp.ui.components.albumsScreen.AlbumsGrid
import com.photogalleryapp.ui.components.albumsScreen.CreateAlbumPopup
import com.photogalleryapp.model.AlbumObject
import com.photogalleryapp.model.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(viewModel: MainViewModel, navController: NavHostController) {
    var showCreateAlbumPopup by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Box(modifier = Modifier.fillMaxSize()) {
        AlbumsGrid(
            viewModel.albums,
            viewModel,
            onAlbumClick = { album ->
                navController.navigate("AlbumContents/${album.id}")
            }
        )
        Button(
            onClick = { showCreateAlbumPopup = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text("Dodaj album")
        }
    }

    if (showCreateAlbumPopup) {
        ModalBottomSheet(
            onDismissRequest = { showCreateAlbumPopup = false },
            sheetState = sheetState
        ) {
            CreateAlbumPopup(
                onConfirm = { albumName, albumColor, albumIcon ->
                    viewModel.insertAlbum(
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
}
