package com.adityamshidlyali.musicwiki.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.adityamshidlyali.musicwiki.model.artistmodel.TopAlbum
import com.adityamshidlyali.musicwiki.network.EndPoints
import com.adityamshidlyali.musicwiki.network.LastfmApi

private const val LAST_FM_STARTING_PAGE_INT = 1

/**
 * Paging3 data source for pagination of Artist's top albums
 */
class ArtistTopAlbumsPagingSource(
    val lastfmApi: LastfmApi,
    private val artist: String
) : PagingSource<Int, TopAlbum>() {

    private val TAG = javaClass.simpleName

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopAlbum> {
        return try {
            val position = params.key ?: 1
            val response =
                lastfmApi.getArtistTopAlbums(
                    EndPoints.API_KEY,
                    EndPoints.FORMAT,
                    EndPoints.ARTIST_TOP_ALBUMS,
                    artist,
                    position,
                    params.loadSize
                )

            val albumsResult = response.body()?.topalbums?.album

            Log.d(TAG, response.body().toString())

            LoadResult.Page(
                data = albumsResult!!,
                prevKey = if (position == LAST_FM_STARTING_PAGE_INT) null else position - 1,
                nextKey = if (albumsResult.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TopAlbum>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}