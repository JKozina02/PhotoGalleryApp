package com.photogalleryapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.photogalleryapp.R
import com.photogalleryapp.model.MainViewModel
import com.photogalleryapp.ui.components.shared.ChangeLanguageButton
import com.photogalleryapp.ui.components.shared.ChangeThemeButton

@Composable
fun GalleryScreen(mainViewModel: MainViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(stringResource(R.string.gallery))
        ChangeLanguageButton(mainViewModel)
        ChangeThemeButton(mainViewModel)
    }
}