package com.photogalleryapp.ui.components.albumsScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import com.photogalleryapp.model.testAlbum0
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.photogalleryapp.model.AlbumObject
import com.photogalleryapp.model.IconRegistry
import com.photogalleryapp.model.testAlbum1

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumPreviewComponent(
    album: AlbumObject,
    isOpen: Boolean,
    onClick: (album: AlbumObject) -> Unit,
    onLongClick: (album: AlbumObject) -> Unit
    )
{
    val iconRegistry = IconRegistry
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .fillMaxWidth()
            .background(Color.Gray)
            .combinedClickable(
                onClick = {
                    onClick(album)
                },
                onLongClick = {
                    onLongClick(album)
                }
            )
            .animateContentSize()
    ){
        Column() {
            AlbumTopBar(
                albumName = album.name,
                albumColor = album.color,
                isOpen = isOpen,
                icon = iconRegistry.getIconById(album.iconID)
            )
            if (isOpen)
            {
                Spacer(Modifier.height(150.dp))
            }
        }
    }
}

@Composable
fun AlbumTopBar(
    albumName: String,
    albumColor: Color,
    isOpen: Boolean,
    icon: ImageVector
    )
{
    Row(
        horizontalArrangement = if(isOpen) Arrangement.Start else Arrangement.End,
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
    ){
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(albumColor)
                .padding(10.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(isOpen){
                Text(albumName)
                Spacer(Modifier.weight(1f))
            }
            Icon(
                imageVector = icon,
                tint = Color.Black,
                modifier = Modifier.size(50.dp),
                contentDescription = "Icon"
            )
        }
    }
}

@Composable
@Preview
fun PreviewHorizontal()
{
    AlbumPreviewComponent(testAlbum0, true, {}, {})
}

@Composable
@Preview
fun PreviewVertical()
{
    AlbumPreviewComponent(testAlbum1, false, {}, {})
}





