package com.adityamshidlyali.musicwiki.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityamshidlyali.musicwiki.model.albummodel.AlbumInfo
import com.adityamshidlyali.musicwiki.network.Resource
import com.adityamshidlyali.musicwiki.repository.AlbumInfoRepository
import com.adityamshidlyali.musicwiki.utility.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * viewmodel to hold the result of album's information from the network calls
 * and also manages the network call events
 */
@HiltViewModel
class AlbumInfoViewModel @Inject constructor(
    private val albumInfoRepository: AlbumInfoRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val TAG = javaClass.simpleName

    sealed class GetAlbumInfoEvent {
        class Success(val albumDetails: AlbumInfo) :
            GetAlbumInfoEvent()

        class Failure(val errorText: String) : GetAlbumInfoEvent()
        object Loading : GetAlbumInfoEvent()
        object Empty : GetAlbumInfoEvent()
    }

    private val _albumInfoStateFlow =
        MutableStateFlow<GetAlbumInfoEvent>(GetAlbumInfoEvent.Empty)
    val albumInfoStateFlow: MutableStateFlow<GetAlbumInfoEvent> = _albumInfoStateFlow

    fun getAlbumInfo(
        api_key: String,
        format: String,
        method: String,
        artist: String,
        album: String
    ) {

        viewModelScope.launch(dispatchers.io) {
            _albumInfoStateFlow.value = GetAlbumInfoEvent.Loading

            when (val genreTagResponse =
                albumInfoRepository.getAlbumInfo(api_key, format, method, artist, album)) {

                is Resource.Error -> {
                    Log.d(TAG, genreTagResponse.message.toString())
                    _albumInfoStateFlow.value =
                        GetAlbumInfoEvent.Failure(genreTagResponse.message.toString())
                }

                is Resource.Success -> {
                    Log.d(TAG, genreTagResponse.data.toString())
                    _albumInfoStateFlow.value =
                        GetAlbumInfoEvent.Success(genreTagResponse.data!!.album)
                }
            }
        }
    }
}