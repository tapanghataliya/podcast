package com.example.podcastapp.data.test

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val added_at_ms: Long,
    val `data`: Data,
    val id: Int,
    val notes: String,
    val type: String
): Parcelable