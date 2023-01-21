package com.adityamshidlyali.musicwiki.model.tagmodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * File contains the tag artists Response POJO,
 * parsed by Retrofit ScalarConverters
 */
@Parcelize
data class TagArtistResponse(
    val topartists: TagArtistList
) : Parcelable

@Parcelize
data class TagArtistList(
    val artist: List<TagArtistInfo>
) : Parcelable

@Parcelize
data class TagArtistInfo(
    val name: String,
    val image: List<TagArtistImage>
) : Parcelable

@Parcelize
data class TagArtistImage(
    @SerializedName("#text")
    val imageLink: String
) : Parcelable