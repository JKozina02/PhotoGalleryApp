package com.photogalleryapp.model

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.graphics.Color
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import androidx.core.net.toUri

class MainViewModelFactory (private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "gallery_db"
            ).build()
            val dao = db.databaseDao()

            return MainViewModel(dao, context.applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class MainViewModel (
    private val dao: DatabaseDao,
    private val context: Context
) : ViewModel() {

    // Dark Theme
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    fun changeDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    // Language
    fun getCurrentLanguage(): String {
        val locales = AppCompatDelegate.getApplicationLocales()
        if (!locales.isEmpty) {
            val tag = locales[0]?.toLanguageTag() ?: "pl"
            android.util.Log.d("MainViewModel", "Current locale tag: $tag")
            return tag.substringBefore("-")
        }
        val def = java.util.Locale.getDefault().language
        android.util.Log.d("MainViewModel", "Current default language: $def")
        return def.substringBefore("-")
    }

    fun setLanguage(languageCode: String) {
        android.util.Log.d("MainViewModel", "Setting language to: $languageCode")
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    // DB
    private val mapper = Mapper()

    val albums: Flow<List<AlbumObject>> =
        dao.getAlbums().map { list -> list.map { mapper.fromDbAlbum(it)} }

    fun getAlbumById(id: Int): Flow<AlbumObject?> =
        dao.getAlbumById(id).map { album -> album?.let { mapper.fromDbAlbum(it) } }

    fun getAllPhotosFromAlbum(albumId: Int): Flow<List<PhotoObject>> =
        dao.getAllPhotosFromAlbum(albumId).map { list -> list.map { mapper.fromDbPhoto(it)} }

    fun getPhotosFromAlbum(albumId: Int, count: Int): Flow<List<PhotoObject>> =
        dao.getPhotosFromAlbum(albumId, count).map { list -> list.map {mapper.fromDbPhoto(it)}}

    fun insertAlbum(album: AlbumObject) {
        viewModelScope.launch {
            dao.insertAlbum(mapper.toDbAlbum(album))
        }
    }

    fun updateAlbumName(id: Int, name: String) {
        viewModelScope.launch {
            dao.updateAlbumName(id, name)
        }
    }


    fun updateAlbumColor(id: Int, color: Color) {
        val colorInt = mapper.fromColor(color)

        viewModelScope.launch {
            dao.updateAlbumColor(id, colorInt)
        }
    }

    fun updateAlbumIcon(id: Int, icon: Int) {
        viewModelScope.launch {
            dao.updateAlbumIcon(id, icon)
        }
    }

    fun deleteAlbum(id: Int) {
        viewModelScope.launch {

            val photos = dao.getPhotosByAlbumIdDirect(id)
            photos.forEach { photo ->
                deletePhysicalFile(photo.uri)
            }
            dao.deleteAlbumById(id)
        }
    }

    fun insertPhoto(photo: PhotoObject) {
        viewModelScope.launch {
            dao.insertPhoto(mapper.toDbPhoto(photo))
        }
    }

    fun insertPhotos(photos: List<PhotoObject>) {
        viewModelScope.launch {
            val dbPhotos = photos.map { mapper.toDbPhoto(it) }
            dao.insertPhotos(dbPhotos)
        }
    }

    fun deletePhoto(id: Int) {
        viewModelScope.launch {
            val photo = dao.getPhotoByIdDirect(id)
            photo?.let {
                deletePhysicalFile(it.uri)
            }
            dao.deletePhotoById(id)
        }
    }

    private fun deletePhysicalFile(uriString: String) {
        try {
            val uri = uriString.toUri()
            val fileProviderAuthority = "${context.packageName}.fileprovider"

            when (uri.scheme) {
                "content" -> {
                    if (uri.authority == fileProviderAuthority) {
                        context.contentResolver.delete(uri, null, null)
                        android.util.Log.d("MainViewModel", "Fizycznie usunięto plik z folderu aplikacji: $uriString")
                    } else {
                        android.util.Log.d("MainViewModel", "Usunięto tylko wpis w bazie danych. Plik na dysku pozostaje bezpieczny: $uriString")
                    }
                }
                "file" -> {
                    val path = uri.path
                    if (path != null) {
                        val file = File(path)
                        val absPath = file.absolutePath

                        val internalRoot = context.applicationInfo.dataDir
                        val externalRoot = context.getExternalFilesDir(null)?.absolutePath?.substringBefore("/files")

                        val isInternal = absPath.startsWith(internalRoot)
                        val isExternal = externalRoot?.let { absPath.startsWith(it) } ?: false
                        
                        if ((isInternal || isExternal) && file.exists()) {
                            file.delete()
                            android.util.Log.d("MainViewModel", "Fizycznie usunięto plik z folderu aplikacji: $absPath")
                        } else {
                            android.util.Log.d("MainViewModel", "Usunięto tylko wpis w bazie danych. Plik na dysku pozostaje bezpieczny: $absPath")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MainViewModel", "Błąd podczas usuwania: $uriString", e)
        }
    }

}