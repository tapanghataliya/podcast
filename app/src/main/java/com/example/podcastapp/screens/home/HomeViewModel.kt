package com.example.podcastapp.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.podcastapp.base.BaseNavigator
import com.example.podcastapp.base.BaseViewModel
import com.example.podcastapp.data.NetworkResult
import com.example.podcastapp.data.Song
import com.example.podcastapp.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository
): BaseViewModel<BaseNavigator>() {

    private var _songResponse = MutableLiveData<NetworkResult<List<Song>>>()
    val songResponse: LiveData<NetworkResult<List<Song>>> = _songResponse

    init {
        fetchAllMovies()
    }

    private fun fetchAllMovies() {
        viewModelScope.launch {
            songRepository.getPopularMovies().collect {
                _songResponse.postValue(it)
            }
        }
    }

}