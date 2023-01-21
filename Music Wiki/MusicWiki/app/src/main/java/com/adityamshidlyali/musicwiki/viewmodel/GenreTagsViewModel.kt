package com.adityamshidlyali.musicwiki.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityamshidlyali.musicwiki.model.GenreTag
import com.adityamshidlyali.musicwiki.network.Resource
import com.adityamshidlyali.musicwiki.repository.GenreTagsRepository
import com.adityamshidlyali.musicwiki.utility.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * viewmodel to hold the result of tags from the network calls
 * and also manages the network call events
 */
@HiltViewModel
class GenreTagsViewModel @Inject constructor(
    private val genreTagsRepository: GenreTagsRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val TAG = javaClass.simpleName

    sealed class GetGenreTagsEvent {
        class Success(val genreTags: List<GenreTag>?) :
            GetGenreTagsEvent()

        class Failure(val errorText: String) : GetGenreTagsEvent()
        object Loading : GetGenreTagsEvent()
        object Empty : GetGenreTagsEvent()
    }

    private val _genreListFetchStateFlow =
        MutableStateFlow<GetGenreTagsEvent>(GetGenreTagsEvent.Empty)
    val genreListFetchStateFlow: MutableStateFlow<GetGenreTagsEvent> = _genreListFetchStateFlow

    fun getGenreTags(
        api_key: String,
        format: String,
        method: String,
    ) {

        viewModelScope.launch(dispatchers.io) {
            _genreListFetchStateFlow.value = GetGenreTagsEvent.Loading

            when (val genreTagResponse =
                genreTagsRepository.getGenreTags(api_key, format, method)) {

                is Resource.Error -> {
                    Log.d(TAG, genreTagResponse.message.toString())
                    _genreListFetchStateFlow.value =
                        GetGenreTagsEvent.Failure(genreTagResponse.message.toString())
                }

                is Resource.Success -> {
                    Log.d(TAG, genreTagResponse.data?.toptags?.tag.toString())
                    _genreListFetchStateFlow.value =
                        GetGenreTagsEvent.Success(genreTagResponse.data?.toptags?.tag)
                }
            }
        }
    }
}