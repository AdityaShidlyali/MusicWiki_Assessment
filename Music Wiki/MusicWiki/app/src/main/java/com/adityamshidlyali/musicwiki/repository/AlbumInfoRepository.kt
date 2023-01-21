package com.adityamshidlyali.musicwiki.repository

import com.adityamshidlyali.musicwiki.model.albummodel.AlbumInfoResponse
import com.adityamshidlyali.musicwiki.network.LastfmApi
import com.adityamshidlyali.musicwiki.network.Resource
import javax.inject.Inject

/**
 * Repository to hold the tag's (Genre's) albums information from network calls
 */
class AlbumInfoRepository @Inject constructor(
    private val lastfmApi: LastfmApi
) {

    private val TAG = javaClass.simpleName

    suspend fun getAlbumInfo(
        api_key: String,
        format: String,
        method: String,
        artist: String,
        album: String
    ): Resource<AlbumInfoResponse> {
        return try {
            val response = lastfmApi.getAlbumInfo(api_key, format, method, artist, album)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An Error occurred")
        }
    }
}