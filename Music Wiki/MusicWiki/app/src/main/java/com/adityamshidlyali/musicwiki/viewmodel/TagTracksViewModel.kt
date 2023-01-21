package com.adityamshidlyali.musicwiki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.adityamshidlyali.musicwiki.network.LastfmApi
import com.adityamshidlyali.musicwiki.paging.TagTracksPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * viewmodel to hold the result of tag's tracks from the Paging source
 */
@HiltViewModel
class TagTracksViewModel @Inject constructor(
    private val lastfmApi: LastfmApi
) : ViewModel() {

    private val TAG = javaClass.simpleName

    fun getTagTracks(tagGenre: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { TagTracksPagingSource(lastfmApi, tagGenre) }
    ).flow.cachedIn(viewModelScope)
}
