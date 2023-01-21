package com.adityamshidlyali.musicwiki.network

/**
 * Contains all static endpoints as string
 * also holds BASE_URL, API_KEY, FORMAT of response as JSON
 */
class EndPoints {
    companion object {

        // API Key
        const val API_KEY = "388bed9f8822511840e11296c64c2cf6"

        // JSON format constant
        const val FORMAT = "json"

        // Base URL
        const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"

        // get top tags
        const val TOP_TAGS = "tag.getTopTags"

        // get tag info
        const val TAG_INFO = "tag.getinfo"

        // get tag albums
        const val TAG_ALBUMS = "tag.gettopalbums"

        // get tag artists
        const val TAG_ARTISTS = "tag.gettopartists"

        // get tag tracks
        const val TAG_TRACKS = "tag.gettoptracks"

        // get album info
        const val ALBUM_INFO = "album.getinfo"

        // get artist info
        const val ARTIST_INFO = "artist.getinfo"

        // get artist top albums
        const val ARTIST_TOP_ALBUMS = "artist.gettopalbums"

        // get artist top tracks
        const val ARTIST_TOP_TRACKS = "artist.gettoptracks"

        // get artist top tags
        const val ARTIST_TOP_TAGS = "artist.gettoptags"
    }
}