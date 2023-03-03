package com.example.podcastapp.api

import javax.inject.Inject

class ApiClient @Inject constructor(private val apiService: ApiService) {

    companion object {
//        const val BASE_URL = "https://imdb-api.com/en/"
        const val BASE_URL = "https://listen-api-test.listennotes.com/api/v2/"
    }

}