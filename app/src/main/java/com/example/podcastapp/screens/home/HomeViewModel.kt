package com.example.podcastapp.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.podcastapp.base.BaseNavigator
import com.example.podcastapp.base.BaseViewModel
import com.example.podcastapp.data.test.SongList
import com.example.podcastapp.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel<BaseNavigator>() {

    private val _songResponse = MutableLiveData<SongList>()
    val users: LiveData<SongList> = _songResponse

    init {
        fetchAllSongs()
    }

    private fun fetchAllSongs() {
        viewModelScope.launch {
            val result = songRepository.getSongs()
            _songResponse.value = result
        }
    }
}

