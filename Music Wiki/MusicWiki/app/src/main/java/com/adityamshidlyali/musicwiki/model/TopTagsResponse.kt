package com.adityamshidlyali.musicwiki.model

/**
 * File contains the tags or genres Response POJO,
 * parsed by Retrofit ScalarConverters
 */
data class TopTagsResponse(
    val toptags: GenreTagsList
)

data class GenreTagsList(
    val tag: List<GenreTag>
)

data class GenreTag(
    val name: String,
)