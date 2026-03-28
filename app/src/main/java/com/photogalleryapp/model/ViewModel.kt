package com.photogalleryapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class ViewModel (application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "gallery_db"
    ).build()

    private val dao = db.databaseDao()
    private val mapper = Mapper()

    val albums: Flow<List<AlbumObject>> =
        dao.getAlbums().map { list -> list.map { it } }

    fun photosFromAlbum(albumId: Int): Flow<List<PhotoObject>> =
        dao.getPhotosFromAlbum(albumId).map { list -> list.map { it } }

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