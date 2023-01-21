package com.adityamshidlyali.musicwiki.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityamshidlyali.musicwiki.model.artistmodel.ArtistInfo
import com.adityamshidlyali.musicwiki.network.Resource
import com.adityamshidlyali.musicwiki.repository.ArtistInfoRepository
import com.adityamshidlyali.musicwiki.utility.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * viewmodel to hold the result of artist's information from the network calls
 * and also manages the network call events
 */
@HiltViewModel
class ArtistInfoViewModel @Inject constructor(
    private val artistInfoRepository: ArtistInfoRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val TAG = javaClass.simpleName

    sealed class GetArtistInfoEvent {
        class Success(val artistInfo: ArtistInfo) :
            GetArtistInfoEvent()

        class Failure(val errorText: String) : GetArtistInfoEvent()
        object Loading : GetArtistInfoEvent()
        object Empty : GetArtistInfoEvent()
    }

    private val _artistInfoStateFlow =
        MutableStateFlow<GetArtistInfoEvent>(GetArtistInfoEvent.Empty)
    val artistInfoStateFlow: MutableStateFlow<GetArtistInfoEvent> = _artistInfoStateFlow

    fun getArtistInfo(
        api_key: String,
        format: String,
        method: String,
        artist: String
    ) {

        viewModelScope.launch(dispatchers.io) {
            _artistInfoStateFlow.value = GetArtistInfoEvent.Loading

            when (val genreTagResponse =
                artistInfoRepository.getArtistInfo(api_key, format, method, artist)) {

                is Resource.Error -> {
                    Log.d(TAG, genreTagResponse.message.toString())
                    _artistInfoStateFlow.value =
                        GetArtistInfoEvent.Failure(genreTagResponse.message.toString())
                }

                is Resource.Success -> {
                    Log.d(TAG, genreTagResponse.data.toString())
                    _artistInfoStateFlow.value =
                        GetArtistInfoEvent.Success(genreTagResponse.data!!.artist)
                }
            }
        }
    }
}