package com.example.podcastapp.data.test

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Podcast(
    val id: String,
    val image: String,
    val listen_score: Int,
    val listen_score_global_rank: String,
    val listennotes_url: String,
    val publisher: String,
    val thumbnail: String,
    val title: String
): Parcelable