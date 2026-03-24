package com.photogalleryapp.model

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

// DB entities - Kotlin Objects
data class PhotoObject(
    val id: Int,
    val albumId: Int,
    val uri: Uri
)
data class AlbumObject(
    val id: Int,
    val name: String,
    val color: Color
)

// DB Entities
@Entity
data class Album(
    @PrimaryKey(autoGenerate = true)val id: Int,
    val name: String,
    val color: Int
)


@Entity(foreignKeys = [ForeignKey(
    entity = Album::class,
    parentColumns = ["id"],
    childColumns = ["albumId"],
    onDelete = ForeignKey.CASCADE
    )]
)
data class Photo(
    @PrimaryKey(autoGenerate = true)val id: Int,
    val albumId: Int,
    val uri: String
)

// Converter class for DB
class Mapper {
    fun fromUri(uri: Uri): String = uri.toString()
    fun toUri(string: String): Uri = Uri.parse(string)
    fun fromColor(color: Color): Int = color.value.toInt()
    fun toColor(value: Int): Color = Color(value)

    fun toDbPhoto(photoObject: PhotoObject): Photo
    {
        return Photo(
            id = photoObject.id,
            albumId = photoObject.albumId,
            uri = fromUri(photoObject.uri)
        )
    }
    fun fromDbPhoto(photo: Photo): PhotoObject
    {
        return PhotoObject(
            id = photo.id,
            albumId = photo.albumId,
            uri = toUri(photo.uri)
        )
    }
    fun toDbAlbum(albumObject: AlbumObject): Album
    {
        return Album(
            id = albumObject.id,
            name = albumObject.name,
            color = fromColor(albumObject.color)
        )
    }
    fun fromDbAlbum(album: Album): AlbumObject
    {
        return AlbumObject(
            id = album.id,
            name = album.name,
            color = toColor(album.color)
        )
    }
}

// DB Queries
@Dao
interface DatabaseDao{
    @Query("SELECT * FROM Album")
    fun getAlbums(): Flow<List<AlbumObject>>

    @Query("SELECT * FROM Photo WHERE albumId IN (:id)")
    fun getPhotosFromAlbum(id: Int): Flow<List<PhotoObject>>

    @Query("UPDATE Album SET color = :color WHERE id = :id")
    suspend fun updateAlbumColor(id: Int, color: Int)

    @Query("UPDATE Album SET name = :name WHERE id = :id")
    suspend fun updateAlbumName(id: Int, name: String)

    @Query("DELETE FROM Album WHERE id = :id")
    suspend fun deleteAlbumById(id: Int)

    @Query("DELETE FROM Photo WHERE id = :id")
    suspend fun deletePhotoById(id: Int)

    @Insert
    suspend fun insertPhotos(photos: List<Photo>): List<Long>

    @Insert
    suspend fun insertPhoto(photo: Photo): Long

    @Insert
    suspend fun insertAlbum(album: Album): Long

}

// DB Object
@Database(entities = [Album::class, Photo::class], version = 1)
    abstract class AppDatabase : RoomDatabase(){
        abstract fun databaseDao(): DatabaseDao
    }