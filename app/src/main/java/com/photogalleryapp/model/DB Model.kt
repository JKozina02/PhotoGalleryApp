package com.photogalleryapp.model

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
    val id: Int? = null,
    val albumId: Int,
    val uri: Uri
)
data class AlbumObject(
    val id: Int? = null,
    val name: String,
    val color: Color,
    val iconID: Int
)

// DB Entities
@Entity
data class Album(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val color: Int,
    val iconID: Int
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
    fun fromColor(color: Color): Int = color.toArgb()
    fun toColor(value: Int): Color = Color(value)

    fun toDbPhoto(photoObject: PhotoObject): Photo
    {
        return Photo(
            id = photoObject.id ?: 0,
            albumId = photoObject.albumId,
            uri = fromUri(photoObject.uri)
        )
    }
    fun fromDbPhoto(photo: Photo): PhotoObject
    {
        return PhotoObject(
            id = photo.id ?: 0,
            albumId = photo.albumId,
            uri = toUri(photo.uri)
        )
    }
    fun toDbAlbum(albumObject: AlbumObject): Album
    {
        return Album(
            id = albumObject.id ?: 0,
            name = albumObject.name,
            color = fromColor(albumObject.color),
            iconID = albumObject.iconID
        )
    }
    fun fromDbAlbum(album: Album): AlbumObject
    {
        return AlbumObject(
            id = album.id,
            name = album.name,
            color = toColor(album.color),
            iconID = album.iconID
        )
    }
}

// DB Queries
@Dao
interface DatabaseDao{
    @Query("SELECT * FROM Album")
    fun getAlbums(): Flow<List<Album>>

    @Query("SELECT * FROM Photo WHERE albumId IN (:albumID)")
    fun getAllPhotosFromAlbum(albumID: Int): Flow<List<Photo>>

    @Query("SELECT * FROM Photo WHERE albumId IN (:albumID) LIMIT (:count)")
    fun getPhotosFromAlbum(albumID: Int, count: Int): Flow<List<Photo>>

    @Query("UPDATE Album SET color = :color WHERE id = :id")
    suspend fun updateAlbumColor(id: Int, color: Int)

    @Query("UPDATE Album SET name = :name WHERE id = :id")
    suspend fun updateAlbumName(id: Int, name: String)

    @Query("UPDATE Album SET iconID = :icon WHERE id = :id")
    suspend fun updateAlbumIcon(id: Int, icon: Int)

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