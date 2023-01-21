package com.adityamshidlyali.musicwiki.repository

import com.adityamshidlyali.musicwiki.model.tagmodel.TagInfoResponse
import com.adityamshidlyali.musicwiki.network.LastfmApi
import com.adityamshidlyali.musicwiki.network.Resource
import javax.inject.Inject

/**
 * Repository to hold the tag's (Genre's) information from network calls
 */
class TagInfoRepository @Inject constructor(
    private val lastfmApi: LastfmApi
) {

    private val TAG = javaClass.simpleName

    suspend fun getTagInfo(
        api_key: String,
        format: String,
        tag: String,
        method: String,
    ): Resource<TagInfoResponse> {
        return try {
            val response = lastfmApi.getTagInfo(api_key, format, tag, method)
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