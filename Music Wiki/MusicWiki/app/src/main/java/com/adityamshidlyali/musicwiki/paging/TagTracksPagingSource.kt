package com.adityamshidlyali.musicwiki.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.adityamshidlyali.musicwiki.model.tagmodel.TagTrackInfo
import com.adityamshidlyali.musicwiki.network.EndPoints
import com.adityamshidlyali.musicwiki.network.LastfmApi

private const val LAST_FM_STARTING_PAGE_INT = 1

/**
 * Paging3 data source for pagination of tag's(Genre's) tracks
 */
class TagTracksPagingSource(
    val lastfmApi: LastfmApi,
    private val tagGenre: String
) : PagingSource<Int, TagTrackInfo>() {

    private val TAG = javaClass.simpleName

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TagTrackInfo> {
        return try {
            val position = params.key ?: 1
            val response =
                lastfmApi.getTagTracks(
                    EndPoints.API_KEY,
                    EndPoints.FORMAT,
                    tagGenre,
                    EndPoints.TAG_TRACKS,
                    position,
                    params.loadSize
                )

            val tagTrackResult = response.body()?.tracks?.track

            Log.d(TAG, tagTrackResult.toString())

            LoadResult.Page(
                data = tagTrackResult!!,
                prevKey = if (position == LAST_FM_STARTING_PAGE_INT) null else position - 1,
                nextKey = if (tagTrackResult.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TagTrackInfo>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}