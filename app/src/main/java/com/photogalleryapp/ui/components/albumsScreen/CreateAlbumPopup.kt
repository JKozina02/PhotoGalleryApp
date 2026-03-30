package com.photogalleryapp.ui.components.albumsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CreateAlbumPopup(
    onConfirm: (String, Color, Int) -> Unit,
    onDismiss: () -> Unit
){
    var albumName by remember {mutableStateOf("")}

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue.copy(alpha = 0.7f))
        )
        {
            Column() {
                Text("Podaj nazwę albumu.")
                TextField(
                    value = albumName,
                    onValueChange = {albumName = it},
                    label =  { Text("Album")},
                    singleLine = true
                )
                Text("Podaj kolor (TODO).")
                Text("Wybierz ikonę (TODO).")
                Row() {
                    Button({
                        onConfirm(albumName, Color.Blue, 0)
                        albumName = ""
                    }){
                        Text("Zapisz")
                    }
                    Button(onDismiss) {
                        Text("Anuluj")
                    }
                }
            }
        }
    }