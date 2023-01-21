package com.adityamshidlyali.musicwiki.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.adityamshidlyali.musicwiki.network.LastfmApi
import com.adityamshidlyali.musicwiki.paging.ArtistTopAlbumsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * viewmodel to hold the result of artist's top albums from the Paging source
 */
@HiltViewModel
class ArtistTopAlbumsViewModel @Inject constructor(
    private val lastfmApi: LastfmApi
) : ViewModel() {

    fun getArtistTopAlbums(artist: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { ArtistTopAlbumsPagingSource(lastfmApi, artist) }
    ).flow.cachedIn(viewModelScope)
}