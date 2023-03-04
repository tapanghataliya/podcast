package com.example.podcastapp.data.test

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongList(
    val description: String,
    val id: String,
    val image: String,
    val items: List<Item>,
    val last_timestamp_ms: Long,
    val listennotes_url: String,
    val name: String,
    val thumbnail: String,
    val total: Int,
    val total_audio_length_sec: Int,
    val type: String,
    val visibility: String
): Parcelable