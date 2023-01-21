package com.adityamshidlyali.musicwiki.model.artistmodel

import com.google.gson.annotations.SerializedName

/**
 * File contains the Artist Info Response POJOs, parsed by Retrofit ScalarConverters
 */
data class ArtistInfoResponse(
    val artist: ArtistInfo
)

data class ArtistInfo(
    val name: String,
    val stats: ArtistStats,
    val bio: ArtistBio,
    val image: List<ArtistInfoImage>
)

data class ArtistStats(
    val listeners: String,
    val playcount: String
)

data class ArtistBio(
    val summary: String
)

data class ArtistInfoImage(
    val size: String,

    @SerializedName("#text")
    val imageLink: String
)