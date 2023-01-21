package com.adityamshidlyali.musicwiki.network

import com.adityamshidlyali.musicwiki.model.TopTagsResponse
import com.adityamshidlyali.musicwiki.model.albummodel.AlbumInfoResponse
import com.adityamshidlyali.musicwiki.model.artistmodel.ArtistInfoResponse
import com.adityamshidlyali.musicwiki.model.artistmodel.ArtistTopAlbumsResponse
import com.adityamshidlyali.musicwiki.model.artistmodel.ArtistTopTagsResponse
import com.adityamshidlyali.musicwiki.model.artistmodel.ArtistTopTracksResponse
import com.adityamshidlyali.musicwiki.model.tagmodel.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This interface contains the network calls queries and functions
 * Implemented by Dagger-Hilt at runtime.
 */
interface LastfmApi {

    @GET(EndPoints.BASE_URL)
    suspend fun getGenreTags(
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("method") method: String
    ): Response<TopTagsResponse>

    @GET(EndPoints.BASE_URL)
    suspend fun getTagInfo(
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("tag") tag: String,
        @Query("method") method: String
    ): Response<TagInfoResponse>

    @GET(EndPoints.BASE_URL)
    suspend fun getTagAlbums(
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("tag") tag: String,
        @Query("method") method: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<TagAlbumsResponse>

    @GET(EndPoints.BASE_URL)
    suspend fun getTagArtists(
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("tag") tag: String,
        @Query("method") method: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<TagArtistResponse>

    @GET(EndPoints.BASE_URL)
    suspend fun getTagTracks(
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("tag") tag: String,
        @Query("method") method: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<TagTracksResponse>

    @GET(EndPoints.BASE_URL)
    suspend fun getAlbumInfo(
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("method") method: String,
        @Query("artist") artist: String,
        @Query("album") album: String
    ): Response<AlbumInfoResponse>

    @GET(EndPoints.BASE_URL)
    suspend fun getArtistInfo(
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("method") method: String,
        @Query("artist") artist: String
    ): Response<ArtistInfoResponse>

    @GET(EndPoints.BASE_URL)
    suspend fun getArtistTopAlbums(
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("method") method: String,
        @Query("artist") artist: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ArtistTopAlbumsResponse>

    @GET(EndPoints.BASE_URL)
    suspend fun getArtistTopTracks(
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("method") method: String,
        @Query("artist") artist: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ArtistTopTracksResponse>

    @GET(EndPoints.BASE_URL)
    suspend fun getArtistTopTags(
        @Query("api_key") api_key: String,
        @Query("format") format: String,
        @Query("method") method: String,
        @Query("artist") artist: String,
    ): Response<ArtistTopTagsResponse>
}