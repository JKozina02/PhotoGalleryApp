package com.photogalleryapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.photogalleryapp.R

data class Library(val name: String, val license: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesScreen(navController: NavHostController) {
    val libraries = listOf(
        Library("Android Jetpack Compose", "Apache 2.0", "Modern toolkit for building native UI."),
        Library("Room Persistence Library", "Apache 2.0", "SQLite object mapping library."),
        Library("Coil", "Apache 2.0", "Image loading library for Android backed by Kotlin Coroutines."),
        Library("Kotlin Coroutines", "Apache 2.0", "Support for lightweight threads."),
        Library("KvColorPicker", "MIT", "A simple and powerful color picker library for Android."),
        Library("Material Design 3", "Apache 2.0", "Google's open-source design system.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.licenses)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(libraries) { lib ->
                ListItem(
                    headlineContent = { Text(lib.name, fontWeight = FontWeight.Bold) },
                    supportingContent = { 
                        Column {
                            Text(lib.description)
                            Text("License: ${lib.license}", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }
}
