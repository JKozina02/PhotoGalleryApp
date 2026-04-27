package com.photogalleryapp.ui.components.albumsScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.photogalleryapp.model.AlbumObject
import com.photogalleryapp.model.IconRegistry
import com.photogalleryapp.model.MainViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun AlbumPreviewComponent(
    album: AlbumObject,
    isOpen: Boolean,
    viewModel: MainViewModel,
    onClick: (album: AlbumObject) -> Unit,
    onLongClick: (album: AlbumObject) -> Unit
) {
    val iconRegistry = IconRegistry
    val photos by viewModel.getPhotosFromAlbum(album.id ?: 0, 5).collectAsState(initial = emptyList())
    val firstPhoto = photos.firstOrNull()

    // 1. Jawna animacja wysokości całego kafelka
    val animatedHeight by animateDpAsState(
        targetValue = if (isOpen) 350.dp else 120.dp,
        animationSpec = tween(durationMillis = 350),
        label = "AlbumHeightAnimation"
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(32.dp))
            .fillMaxWidth()
            .height(animatedHeight)
            .background(Color.DarkGray)
            .combinedClickable(
                onClick = { onClick(album) },
                onLongClick = { onLongClick(album) }
            )
    ) {
        // 2. AnimatedContent zapewnia płynne przejście tła (zdjęcie -> karuzela)
        AnimatedContent(
            targetState = isOpen,
            transitionSpec = {
                fadeIn(animationSpec = tween(350)) togetherWith fadeOut(animationSpec = tween(350))
            },
            label = "AlbumContentTransition"
        ) { targetOpen ->
            if (targetOpen && photos.isNotEmpty()) {
                val pagerState = rememberPagerState(pageCount = { photos.size })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    pageSpacing = 0.dp
                ) { page ->
                    AsyncImage(
                        model = photos[page].uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                if (firstPhoto != null) {
                    AsyncImage(
                        model = firstPhoto.uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray))
                }
            }
        }

        // 3. Nakładka z animowanym paskiem górnym
        AlbumTopBar(
            albumName = album.name,
            albumColor = album.color,
            isOpen = isOpen,
            icon = iconRegistry.getIconById(album.iconID)
        )
    }
}

@Composable
fun AlbumTopBar(
    albumName: String,
    albumColor: Color,
    isOpen: Boolean,
    icon: ImageVector
) {
    val horizontalBias by animateFloatAsState(
        targetValue = if (isOpen) -1f else 1f,
        animationSpec = tween(durationMillis = 350),
        label = "TopBarPosition"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .align(BiasAlignment(horizontalBias, 0f))
                .clip(RoundedCornerShape(16.dp))
                .background(albumColor.copy(alpha = 0.85f))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .animateContentSize(tween(durationMillis = 350)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isOpen) {
                Text(
                    text = albumName,
                    color = Color.Black,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Spacer(Modifier.weight(1f, fill = false))
            }
            Icon(
                imageVector = icon,
                tint = Color.Black,
                modifier = Modifier.size(if (isOpen) 28.dp else 36.dp),
                contentDescription = "Icon"
            )
        }
    }
}
