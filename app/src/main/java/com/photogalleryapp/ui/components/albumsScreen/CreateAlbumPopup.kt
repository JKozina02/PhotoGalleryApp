package com.photogalleryapp.ui.components.albumsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kavi.droid.color.picker.ui.pickers.GridColorPicker
import com.photogalleryapp.model.IconRegistry
import com.photogalleryapp.ui.components.shared.HorizontalDividerWithSubhead

@Composable
fun CreateAlbumPopup(
    onConfirm: (String, Color, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val icons = IconRegistry
    var albumName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color.Gray) }
    var selectedIconId by remember { mutableStateOf(icons.icons.keys.firstOrNull() ?: 0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            HorizontalDividerWithSubhead("Name")

            OutlinedTextField(
                value = albumName,
                onValueChange = { albumName = it },
                label = { Text("Album Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            HorizontalDividerWithSubhead("Appearance")

            Column {
                Text(
                    "Highlight Color:",
                    fontWeight = FontWeight.Bold
                )

                GridColorPicker(
                    onColorSelected = {
                        selectedColor = it
                    },
                    modifier = Modifier.background(Color.Transparent)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    "Icon:",
                    fontWeight = FontWeight.Bold
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(icons.icons.entries.toList()) { iconEntry ->
                        val isSelected = selectedIconId == iconEntry.key

                        Icon(
                            imageVector = icons.getIconById(iconEntry.key),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { selectedIconId = iconEntry.key }
                                .background(
                                    if (isSelected) Color.LightGray
                                    else Color.Transparent
                                )
                                .clip(CircleShape)
                                .padding(8.dp),
                            tint = Color.Black
                        )
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    if (albumName.isNotBlank()) {
                        onConfirm(albumName, selectedColor, selectedIconId)
                    }
                },
                enabled = albumName.isNotBlank()
            ) {
                Text("Create")
            }
        }
    }
}
