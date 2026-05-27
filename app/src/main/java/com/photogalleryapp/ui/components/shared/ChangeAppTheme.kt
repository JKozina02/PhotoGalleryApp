package com.photogalleryapp.ui.components.shared

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.photogalleryapp.R
import com.photogalleryapp.model.MainViewModel

@Composable
fun ChangeThemeButton(mainViewModel: MainViewModel){
    Button(
        onClick = {
            mainViewModel.changeDarkTheme()
        }
    ) {
        Text(text = stringResource(R.string.change_theme))
    }
}