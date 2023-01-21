package com.adityamshidlyali.musicwiki.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityamshidlyali.musicwiki.model.artistmodel.TopTagList
import com.adityamshidlyali.musicwiki.network.Resource
import com.adityamshidlyali.musicwiki.repository.ArtistTopTagsRepository
import com.adityamshidlyali.musicwiki.utility.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * viewmodel to hold the result of artist's top tags from the network calls
 * and also manages the network call events
 */
@HiltViewModel
class ArtistTopTagsViewModel @Inject constructor(
    private val artistTopTagsRepository: ArtistTopTagsRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {
    private val TAG = javaClass.simpleName

    sealed class GetArtistTopTagsEvent {
        class Success(val topTagList: TopTagList) :
            GetArtistTopTagsEvent()

        class Failure(val errorText: String) : GetArtistTopTagsEvent()
        object Loading : GetArtistTopTagsEvent()
        object Empty : GetArtistTopTagsEvent()
    }

    private val _artistTopTagsStateFlow =
        MutableStateFlow<GetArtistTopTagsEvent>(GetArtistTopTagsEvent.Empty)
    val artistTopTagsStateFlow: MutableStateFlow<GetArtistTopTagsEvent> = _artistTopTagsStateFlow

    fun getArtistTopTags(
        api_key: String,
        format: String,
        method: String,
        artist: String,
    ) {

        viewModelScope.launch(dispatchers.io) {
            _artistTopTagsStateFlow.value = GetArtistTopTagsEvent.Loading

            when (val genreTagResponse =
                artistTopTagsRepository.getArtistTopTags(
                    api_key,
                    format,
                    method,
                    artist,
                )) {

                is Resource.Error -> {
                    Log.d(TAG, genreTagResponse.message.toString())
                    _artistTopTagsStateFlow.value =
                        GetArtistTopTagsEvent.Failure(genreTagResponse.message.toString())
                }

                is Resource.Success -> {
                    Log.d(TAG, genreTagResponse.data.toString())
                    _artistTopTagsStateFlow.value =
                        GetArtistTopTagsEvent.Success(genreTagResponse.data!!.toptags)
                }
            }
        }
    }
}