package com.example.podcastapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataList(
//    @SerializedName("id") val id: String,
//    @SerializedName("link") val link: String,
//    @SerializedName("audio") val audio: String,
//    @SerializedName("image") val image: String,
//    @SerializedName("title") val title: String,
//    @SerializedName("thumbnail") val thumbnail: String,
//    @SerializedName("description") val description: String,
//    @SerializedName("type") val type: String,
//    @SerializedName("notes") val notes: String,
    var id: String? = null,
    var link: String? = null,
    var audio: String? = null,
    var image: String? = null,
    var title: String? = null,
    var thumbnail: String? = null,
    var description: String? = null,
    var type: String? = null,
    var notes: String? = null,
) : Parcelable
