package com.adityamshidlyali.musicwiki.model.tagmodel

/**
 * File contains the tag info Response POJO,
 * parsed by Retrofit ScalarConverters
 */
data class TagInfoResponse(
    val tag: TagInfoWiki
)

data class TagInfoWiki(
    val wiki: TagInfoSummary
)

data class TagInfoSummary(
    val summary: String
)