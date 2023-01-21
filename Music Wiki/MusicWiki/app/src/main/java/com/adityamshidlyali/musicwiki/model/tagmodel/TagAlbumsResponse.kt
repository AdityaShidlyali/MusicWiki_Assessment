package com.adityamshidlyali.musicwiki.model.tagmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * File contains the tag album Response POJOs,
 * parsed by Retrofit ScalarConverters
 */
@Parcelize
data class TagAlbumsResponse(
    val albums: TagAlbum
) : Parcelable

@Parcelize
data class TagAlbum(
    val album: List<TagAlbumInfo>
) : Parcelable

@Parcelize
data class TagAlbumInfo(
    val name: String,
    val artist: TagAlbumArtist,
    val image: List<TagAlbumImage>
) : Parcelable

@Parcelize
data class TagAlbumArtist(
    val name: String
) : Parcelable

@Parcelize
data class TagAlbumImage(
    @SerializedName("#text")
    val imageLink: String
) : Parcelable