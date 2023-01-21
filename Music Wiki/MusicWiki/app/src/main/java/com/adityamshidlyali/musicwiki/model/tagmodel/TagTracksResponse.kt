package com.adityamshidlyali.musicwiki.model.tagmodel

import com.google.gson.annotations.SerializedName

/**
 * File contains the tag tracks Response POJO,
 * parsed by Retrofit ScalarConverters
 */
data class TagTracksResponse(
    val tracks: TagTrack
)

data class TagTrack(
    val track: List<TagTrackInfo>
)

data class TagTrackInfo(
    val name: String,
    val artist: TagTrackArtist,
    val image: List<TagTrackImage>
)

data class TagTrackArtist(
    val name: String
)

data class TagTrackImage(
    @SerializedName("#text")
    val imageLink: String
)