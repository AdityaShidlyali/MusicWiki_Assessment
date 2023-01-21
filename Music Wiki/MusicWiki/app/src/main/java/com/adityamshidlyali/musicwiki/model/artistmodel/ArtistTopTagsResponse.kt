package com.adityamshidlyali.musicwiki.model.artistmodel

/**
 * File contains the Artist's top tags(Genres) Response POJOs,
 * parsed by Retrofit ScalarConverters
 */
data class ArtistTopTagsResponse(
    val toptags: TopTagList
)

data class TopTagList(
    val tag: List<TopTagInfo>
)

data class TopTagInfo(
    val name: String
)