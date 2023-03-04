package com.example.podcastapp.api

import com.example.podcastapp.data.SongResponse
import com.example.podcastapp.data.test.SongList
import retrofit2.http.GET

interface ApiService {
//    @GET("API/MostPopularMovies/k_9v5jw2c1")
    @GET("playlists/:id?type=episode_list&last_timestamp_ms=0&sort=recent_added_first")
//    suspend fun getSongList(): SongResponse
    suspend fun getSongList(): SongList
}