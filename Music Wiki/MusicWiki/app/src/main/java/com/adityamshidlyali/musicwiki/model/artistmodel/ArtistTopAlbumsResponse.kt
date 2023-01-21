package com.adityamshidlyali.musicwiki.model.artistmodel

import com.google.gson.annotations.SerializedName

/**
 * File contains the Artist's top albums Response POJOs,
 * parsed by Retrofit ScalarConverters
 */
data class ArtistTopAlbumsResponse(
    val topalbums: TopAlbums
)

data class TopAlbums(
    val album: List<TopAlbum>
)

data class TopAlbum(
    val name: String,
    val image: List<TopAlbumImage>
)

data class TopAlbumImage(
    val size: String,

    @SerializedName("#text")
    val imageLink: String
)
