@file:OptIn(ExperimentalMaterial3Api::class)

package com.photogalleryapp.ui.components.shared

import android.graphics.drawable.shapes.Shape
import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.sqlite.throwSQLiteException
import com.photogalleryapp.model.AlbumObject
import com.photogalleryapp.model.IconRegistry
import com.photogalleryapp.model.testAlbum0
import org.w3c.dom.Text

@Composable
fun AlbumEditorPopup(
    album: AlbumObject,
    onDismissRequest: () -> Unit,
    onSaveRequest: () -> Unit
) {
    val icons = IconRegistry
    var showDialog by remember {mutableStateOf(false)}

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            HorizontalDividerWithSubhead("Name")

            TextField(
                value = album.name,
                onValueChange = { /*TODO()*/ },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Column(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            HorizontalDividerWithSubhead("Appearance")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Icon:",
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = icons.getIconById(album.iconID),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            showDialog = true
                        },
                    tint = Color.Black
                )
            }

            Row() {
                Text(
                    "Highlight Color:",
                    fontWeight = FontWeight.Bold
                )

                Text(album.color.toString())
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
                Text("Opcja 1")
            }
            Button(
                onClick = { onSaveRequest() }
            ) {
                Text("Opcja 2")
            }
        }
    }

    if (showDialog) {
        IconSelectorComponent(
            onDismissRequest = {
                showDialog = false
            }
        )
    }
}

@Composable
fun HorizontalDividerWithSubhead(
    text: String
) {
    Column {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }

}

@Composable
fun IconSelectorComponent(
    onDismissRequest: () -> Unit,
) {
    val icons = IconRegistry

    var selectedIcon by remember { mutableStateOf<Int?>(null) }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Select album icon: $selectedIcon",
                    fontSize = 24.sp
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(6)
                ) {
                    items(icons.icons.entries.toList()) { iconEntry ->
                        val isSelected = selectedIcon == iconEntry.key

                        Icon(
                            imageVector = icons.getIconById(iconEntry.key),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { selectedIcon = iconEntry.key }
                                .background(
                                    if (isSelected) Color.Gray
                                    else Color.Transparent
                                )
                                .clip(CircleShape),
                            tint = Color.Black
                        )
                    }
                }

                Box() {
                    TextButton(
                        onClick = { }
                    ) {
                        Text(
                            "Add new"
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() }
                        ) {
                            Text(
                                "Odrzuć"
                            )
                        }

                        TextButton(
                            onClick = { }
                        ) {
                            Text(
                                "Zatwierdź"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAlbumEditorPopup() {
    ModalBottomSheet(onDismissRequest = { }) {
        AlbumEditorPopup(testAlbum0, { }, { })
    }
}

@Preview
@Composable
fun PreviewIconSelectorComponent() {
    IconSelectorComponent({ })
}