package com.example.podcastapp.repository

import com.example.podcastapp.api.ApiService
import com.example.podcastapp.data.test.SongList
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getSongs(): SongList {
        return apiService.getSongList()
    }
}