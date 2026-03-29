package com.photogalleryapp.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.photogalleryapp.model.AlbumObject
import kotlinx.coroutines.flow.Flow

@Composable
fun AlbumsGrid(albumsFlow: Flow<List<AlbumObject>>){
    val albums = albumsFlow.collectAsState(initial = emptyList())
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)
    ) {
        items(albums.value){ album ->
            AlbumPreview(album)
        }
    }
}

@Composable
fun AlbumPreview(album: AlbumObject)
{
    Box(
        Modifier.background(
            color = album.color
        ),
        contentAlignment = Alignment.Center
    ){
        Text(album.name)
    }
}