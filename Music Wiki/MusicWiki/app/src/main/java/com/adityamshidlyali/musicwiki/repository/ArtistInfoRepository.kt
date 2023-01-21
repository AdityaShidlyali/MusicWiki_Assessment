package com.adityamshidlyali.musicwiki.repository

import com.adityamshidlyali.musicwiki.model.artistmodel.ArtistInfoResponse
import com.adityamshidlyali.musicwiki.network.LastfmApi
import com.adityamshidlyali.musicwiki.network.Resource
import javax.inject.Inject

/**
 * Repository to hold the artist's albums information from network calls
 */
class ArtistInfoRepository @Inject constructor(
    private val lastfmApi: LastfmApi
) {

    private val TAG = javaClass.simpleName

    suspend fun getArtistInfo(
        api_key: String,
        format: String,
        method: String,
        artist: String
    ): Resource<ArtistInfoResponse> {
        return try {
            val response = lastfmApi.getArtistInfo(api_key, format, method, artist)
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