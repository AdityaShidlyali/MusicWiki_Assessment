package com.adityamshidlyali.musicwiki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.adityamshidlyali.musicwiki.network.LastfmApi
import com.adityamshidlyali.musicwiki.paging.TagArtistPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * viewmodel to hold the result of tag's artists from the Paging source
 */
@HiltViewModel
class TagArtistsViewModel @Inject constructor(
    private val lastfmApi: LastfmApi
) : ViewModel() {

    private val TAG = javaClass.simpleName

    fun getTagArtists(tagGenre: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { TagArtistPagingSource(lastfmApi, tagGenre) }
    ).flow.cachedIn(viewModelScope)
}