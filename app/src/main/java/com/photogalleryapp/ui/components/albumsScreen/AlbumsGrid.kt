package com.photogalleryapp.ui.components.albumsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.photogalleryapp.model.AlbumObject
import kotlinx.coroutines.flow.Flow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.photogalleryapp.model.MainViewModel
import com.photogalleryapp.ui.components.shared.AlbumEditorPopup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsGrid(albumsFlow: Flow<List<AlbumObject>>, view: MainViewModel){
    val albums = albumsFlow.collectAsState(initial = emptyList())
    val selectedAlbum = remember { mutableStateOf<AlbumObject?>(null) }

    var showAlbumEditModal by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(albums.value){ album ->
            AlbumPreviewComponent(
                album = album,
                isOpen = album == selectedAlbum.value,
                onClick = {
                    selectedAlbum.value = album
                },
                onLongClick = {
                    selectedAlbum.value = album
                    showAlbumEditModal = true
                }
            )

        }
    }

    if (showAlbumEditModal) {
        ModalBottomSheet(
            onDismissRequest =  {
                showAlbumEditModal = false
            }
        ) {
            selectedAlbum.value?.let { album ->
                AlbumEditorPopup(
                    album = album,
                    onDismissRequest = { showAlbumEditModal = false },
                    onSaveRequest = { updated ->
                        updated.id?.let { id ->
                            view.updateAlbumName(id, updated.name)
                            view.updateAlbumColor(id, updated.color)
                            view.updateAlbumIcon(id, updated.iconID)
                        }
                        showAlbumEditModal = false
                    }
                )
            }
        }
    }
}