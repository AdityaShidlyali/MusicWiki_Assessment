package com.adityamshidlyali.musicwiki.repository

import com.adityamshidlyali.musicwiki.model.artistmodel.ArtistTopTagsResponse
import com.adityamshidlyali.musicwiki.network.LastfmApi
import com.adityamshidlyali.musicwiki.network.Resource
import javax.inject.Inject

/**
 * Repository to hold the artist's top tags information from network calls
 */
class ArtistTopTagsRepository @Inject constructor(
    private val lastfmApi: LastfmApi
) {

    private val TAG = javaClass.simpleName

    suspend fun getArtistTopTags(
        api_key: String,
        format: String,
        method: String,
        artist: String,
    ): Resource<ArtistTopTagsResponse> {
        return try {
            val response =
                lastfmApi.getArtistTopTags(api_key, format, method, artist)
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