package com.photogalleryapp.ui.components.shared

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.photogalleryapp.R
import com.photogalleryapp.model.MainViewModel

@Composable
fun ChangeLanguageButton(mainViewModel: MainViewModel) {
    Button(onClick = {
        val nextLanguage = if (mainViewModel.getCurrentLanguage() == "pl") "en" else "pl"
        mainViewModel.setLanguage(nextLanguage)
    }) {
        Text(text = stringResource(R.string.change_language))
    }
}
