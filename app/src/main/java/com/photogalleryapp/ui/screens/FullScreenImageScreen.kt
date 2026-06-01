package com.photogalleryapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.photogalleryapp.R
import com.photogalleryapp.model.MainViewModel
import androidx.core.net.toUri
import android.provider.OpenableColumns
import android.graphics.BitmapFactory
import androidx.compose.ui.unit.dp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenImageScreen(
    photoId: Int,
    photoUri: String,
    viewModel: MainViewModel,
    navController: NavHostController
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPhotoInformationDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_confirm_title)) },
            text = { Text(stringResource(R.string.delete_confirm_msg)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePhoto(photoId)
                        showDeleteDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text(stringResource(R.string.delete), color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    if (showPhotoInformationDialog) {
        val context = LocalContext.current
        val uri = photoUri.toUri()
        
        var name by remember { mutableStateOf("Unknown") }
        var size by remember { mutableStateOf("Unknown") }
        var type by remember { mutableStateOf("Unknown") }
        var resolution by remember { mutableStateOf<Pair<Int, Int>?>(null) }

        LaunchedEffect(uri) {
            try {
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (cursor.moveToFirst()) {
                        if (nameIndex != -1) name = cursor.getString(nameIndex) ?: "Unknown"
                        if (sizeIndex != -1) {
                            val sizeBytes = cursor.getLong(sizeIndex)
                            size = formatFileSize(sizeBytes)
                        }
                    }
                }
                type = context.contentResolver.getType(uri) ?: "Unknown"

                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                    }
                    BitmapFactory.decodeStream(inputStream, null, options)
                    if (options.outWidth != -1 && options.outHeight != -1) {
                        resolution = Pair(options.outWidth, options.outHeight)
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("FullScreenImage", "Error fetching photo info", e)
            }
        }

        AlertDialog(
            onDismissRequest = { showPhotoInformationDialog = false },
            title = { Text(stringResource(R.string.photo_info_title)) },
            text = {
                Column {
                    Text(stringResource(R.string.photo_info_name, name))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.photo_info_size, size))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.photo_info_type, type))
                    resolution?.let { (width, height) ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.photo_info_resolution, width, height))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPhotoInformationDialog = false }) {
                    Text(stringResource(R.string.close))
                }
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
                    }
                },
                actions = {
                    IconButton(onClick = { showPhotoInformationDialog = true }) {
                        Icon(Icons.Default.Info, contentDescription = stringResource(R.string.info))
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_photo))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.5f),
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = photoUri.toUri(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

fun formatFileSize(size: Long): String {
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format(Locale.US, "%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}
