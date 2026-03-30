package com.photogalleryapp.ui.components.albumsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.photogalleryapp.model.AlbumObject
import kotlinx.coroutines.flow.Flow
import androidx.compose.runtime.mutableStateOf

@Composable
fun AlbumsGrid(albumsFlow: Flow<List<AlbumObject>>){
    val albums = albumsFlow.collectAsState(initial = emptyList())
    val selectedAlbum = remember { mutableStateOf<AlbumObject?>(null) }
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(albums.value){ album ->
            AlbumPreviewComponent(
                album = album,
                isOpen = album == selectedAlbum.value,
                onClick = {selectedAlbum.value = album}
            )

        }
    }
}