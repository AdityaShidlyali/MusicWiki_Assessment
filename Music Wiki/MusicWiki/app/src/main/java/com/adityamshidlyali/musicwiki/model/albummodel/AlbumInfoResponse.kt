package com.adityamshidlyali.musicwiki.model.albummodel

import com.google.gson.annotations.SerializedName

/**
 * File contains the Album Info Response POJOs,
 * parsed by Retrofit ScalarConverters
 */
data class AlbumInfoResponse(
    val album: AlbumInfo
)

data class AlbumInfo(
    val artist: String,
    val wiki: AlbumSummary,
    val image: List<AlbumInfoImage>,
    val tags: AlbumInfoTag
)

data class AlbumInfoTag(
    val tag: List<AlbumTags>
)

data class AlbumTags(
    val name: String
)

data class AlbumInfoImage(
    val size: String,

    @SerializedName("#text")
    val link: String
)

data class AlbumSummary(
    val published: String,
    val summary: String
)