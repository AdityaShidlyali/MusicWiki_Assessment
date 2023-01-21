package com.adityamshidlyali.musicwiki.repository

import com.adityamshidlyali.musicwiki.model.TopTagsResponse
import com.adityamshidlyali.musicwiki.network.LastfmApi
import com.adityamshidlyali.musicwiki.network.Resource
import javax.inject.Inject

/**
 * Repository to hold the tag's (Genre's) from network calls
 */
class GenreTagsRepository @Inject constructor(
    private val lastfmApi: LastfmApi
) {

    private val TAG = javaClass.simpleName

    suspend fun getGenreTags(
        api_key: String,
        format: String,
        method: String,
    ): Resource<TopTagsResponse> {
        return try {
            val response = lastfmApi.getGenreTags(api_key, format, method)
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