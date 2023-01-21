package com.adityamshidlyali.musicwiki.model.artistmodel

import com.google.gson.annotations.SerializedName

/**
 * File contains the Artist's top tracks Response POJO,
 * parsed by Retrofit ScalarConverters
 */
data class ArtistTopTracksResponse(
    val toptracks: TopTracks
)

data class TopTracks(
    val track: List<TopTrack>
)

data class TopTrack(
    val name: String,
    val image: List<TopTrackImage>
)

data class TopTrackImage(
    val size: String,

    @SerializedName("#text")
    val imageLink: String
)