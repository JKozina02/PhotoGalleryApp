package com.photogalleryapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.res.stringResource
import com.photogalleryapp.R
import com.photogalleryapp.model.AlbumObject
import com.photogalleryapp.model.IconRegistry
import com.photogalleryapp.model.MainViewModel
import com.photogalleryapp.ui.components.albumsScreen.AlbumsGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: MainViewModel, onAlbumClick: (AlbumObject) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedIconId by remember { mutableStateOf<Int?>(null) }
    val showFilterDialog = remember { mutableStateOf(false) }

    val allAlbums by viewModel.albums.collectAsState(initial = emptyList())

    val filteredAlbums = remember(searchQuery, selectedIconId, allAlbums) {
        allAlbums.filter { album ->
            val matchesName = album.name.contains(searchQuery, ignoreCase = true)
            val matchesIcon = selectedIconId == null || album.iconID == selectedIconId
            matchesName && matchesIcon
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.clear_search))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box {
                IconButton(
                    onClick = { showFilterDialog.value = true },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (selectedIconId != null) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    )
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = stringResource(R.string.filter))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredAlbums.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.no_albums_found), style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            val filteredFlow = remember(filteredAlbums) {
                kotlinx.coroutines.flow.flowOf(filteredAlbums)
            }
            
            AlbumsGrid(
                albumsFlow = filteredFlow,
                view = viewModel,
                modifier = Modifier.fillMaxSize(),
                onAlbumClick = onAlbumClick
            )
        }
    }

    if (showFilterDialog.value) {
        IconFilterDialog(
            selectedIconId = selectedIconId,
            onIconSelected = { id ->
                selectedIconId = id
                showFilterDialog.value = false
            },
            onDismiss = { showFilterDialog.value = false }
        )
    }
}

@Composable
fun IconFilterDialog(
    selectedIconId: Int?,
    onIconSelected: (Int?) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.filter_by_icon),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterIconItem(
                            icon = Icons.Default.Clear,
                            label = stringResource(R.string.all_icons),
                            isSelected = selectedIconId == null,
                            onClick = { onIconSelected(null) }
                        )
                    }

                    items(IconRegistry.icons.toList()) { (id, icon) ->
                        FilterIconItem(
                            icon = icon,
                            label = null,
                            isSelected = selectedIconId == id,
                            onClick = { onIconSelected(id) }
                        )
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

@Composable
fun FilterIconItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
