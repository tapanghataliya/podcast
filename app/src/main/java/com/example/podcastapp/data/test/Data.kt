package com.example.podcastapp.data.test

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val audio: String,
    val audio_length_sec: Int,
    val description: String,
    val error: String,
    val explicit_content: Boolean,
    val guid_from_rss: String,
    val id: String,
    val image: String,
    val link: String,
    val listennotes_edit_url: String,
    val listennotes_url: String,
    val maybe_audio_invalid: Boolean,
    val podcast: Podcast,
    val pub_date_ms: Long,
    val status: String,
    val thumbnail: String,
    val title: String
): Parcelable