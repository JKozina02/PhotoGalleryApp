package com.photogalleryapp.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModelFactory (private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "gallery_db"
            ).build()
            val dao = db.databaseDao()

            return MainViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
class MainViewModel (
    private val dao: DatabaseDao
) : ViewModel() {
    private val mapper = Mapper()

    val albums: Flow<List<AlbumObject>> =
        dao.getAlbums().map { list -> list.map { mapper.fromDbAlbum(it)} }

    fun photosFromAlbum(albumId: Int): Flow<List<PhotoObject>> =
        dao.getPhotosFromAlbum(albumId).map { list -> list.map { mapper.fromDbPhoto(it)} }

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

    fun updateAlbumColor(id: Int, color: Int) {
        viewModelScope.launch {
            dao.updateAlbumColor(id, color)
        }
    }

    fun deleteAlbum(id: Int) {
        viewModelScope.launch {
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

    // 🔹 Delete photo
    fun deletePhoto(id: Int) {
        viewModelScope.launch {
            dao.deletePhotoById(id)
        }
    }

}