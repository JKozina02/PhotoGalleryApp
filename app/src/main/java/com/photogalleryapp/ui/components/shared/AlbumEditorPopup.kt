@file:OptIn(ExperimentalMaterial3Api::class)

package com.photogalleryapp.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kavi.droid.color.picker.ui.pickers.GridColorPicker
import com.photogalleryapp.model.AlbumObject
import com.photogalleryapp.model.IconRegistry
import com.photogalleryapp.model.testAlbum0

@Composable
fun AlbumEditorPopup(
    album: AlbumObject,
    onDismissRequest: () -> Unit,
    onSaveRequest: (AlbumObject) -> Unit
) {
    val icons = IconRegistry
    var draft by remember { mutableStateOf(album) }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            HorizontalDividerWithSubhead("Name")

            OutlinedTextField(
                value = draft.name,
                onValueChange = { draft = draft.copy(name = it) },
                modifier = Modifier.fillMaxWidth(),
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
                        draft = draft.copy(color = it)
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
                    columns = GridCells.Fixed(6)
                ) {
                    items(icons.icons.entries.toList()) { iconEntry ->
                        val isSelected = draft.iconID == iconEntry.key

                        Icon(
                            imageVector = icons.getIconById(iconEntry.key),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { draft = draft.copy(iconID = iconEntry.key) }
                                .background(
                                    if (isSelected) Color.Gray
                                    else Color.Transparent
                                )
                                .clip(CircleShape),
                            tint = Color.Black
                        )
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onDismissRequest() }
            ) {
                Text("Back")
            }
            Button(
                onClick = { onSaveRequest(draft) }
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun HorizontalDividerWithSubhead(
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        HorizontalDivider()
    }

}


@Preview
@Composable
fun PreviewAlbumEditorPopup() {
    var album = testAlbum0
    ModalBottomSheet(onDismissRequest = { }) {
    Text(album.iconID.toString())

        AlbumEditorPopup(
            album,
            { },
            onSaveRequest = { updated ->
                album = updated
            }
        )
    }
}
