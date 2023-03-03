package com.example.podcastapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    var id: String?,
    var data: DataList,
    var type: String?,
    var notes: String?,
): Parcelable

data class SongResponse(val items: List<Song>, val errorMessage: String)

sealed class NetworkResult<T> {
    data class Loading<T>(val isLoading: Boolean) : NetworkResult<T>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Failure<T>(val errorMessage: String) : NetworkResult<T>()
}