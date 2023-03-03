package com.example.podcastapp.repository

import com.example.podcastapp.api.ApiService
import com.example.podcastapp.data.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPopularMovies() = flow {
        emit(NetworkResult.Loading(true))
        val response = apiService.getSongList()
        emit(NetworkResult.Success(response.items))
    }.catch { e ->
        emit(NetworkResult.Failure(e.message ?: "Unknown Error"))
    }
}