package com.adityamshidlyali.musicwiki.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityamshidlyali.musicwiki.network.Resource
import com.adityamshidlyali.musicwiki.repository.TagInfoRepository
import com.adityamshidlyali.musicwiki.utility.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * viewmodel to hold the result of tag's details the network calls
 * and also manages the network call events
 */
@HiltViewModel
class GenreTagDetailsViewModel @Inject public constructor(
    private val tagInfoRepository: TagInfoRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val TAG = javaClass.simpleName

    sealed class GetGenreTagDetailsEvent {
        class Success(val genreTagSummary: String) :
            GetGenreTagDetailsEvent()

        class Failure(val errorText: String) : GetGenreTagDetailsEvent()
        object Loading : GetGenreTagDetailsEvent()
        object Empty : GetGenreTagDetailsEvent()
    }

    private val _genreTagDetailsStateFlow =
        MutableStateFlow<GetGenreTagDetailsEvent>(GetGenreTagDetailsEvent.Empty)
    val genreTagDetailsStateFlow: MutableStateFlow<GetGenreTagDetailsEvent> =
        _genreTagDetailsStateFlow

    fun getTagInfo(
        api_key: String,
        format: String,
        tag: String,
        method: String,
    ) {

        viewModelScope.launch(dispatchers.io) {
            _genreTagDetailsStateFlow.value = GetGenreTagDetailsEvent.Loading

            when (val genreTagResponse =
                tagInfoRepository.getTagInfo(api_key, format, tag, method)) {

                is Resource.Error -> {
                    Log.d(TAG, genreTagResponse.message.toString())
                    _genreTagDetailsStateFlow.value =
                        GetGenreTagDetailsEvent.Failure(genreTagResponse.message.toString())
                }

                is Resource.Success -> {
                    Log.d(TAG, genreTagResponse.data?.tag?.wiki?.summary.toString())
                    _genreTagDetailsStateFlow.value =
                        GetGenreTagDetailsEvent.Success(genreTagResponse.data!!.tag.wiki.summary.toString())
                }
            }
        }
    }
}