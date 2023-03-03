package com.example.podcastapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.example.podcastapp.R
import com.example.podcastapp.screens.podcast_detail.PodcastFragment
import com.example.podcastapp.screens.podcast_detail.PodcastFragmentArgs
import java.io.ByteArrayInputStream
import java.io.InputStream

class Music {

    companion object {
        fun setSongPosition(increment: Boolean) {

            if (increment) {
                if (PodcastFragment.musicListPA.size - 1 == PodcastFragment.songPosition)
                    PodcastFragment.songPosition = 0
                else ++PodcastFragment.songPosition
            } else {
                if (0 == PodcastFragment.songPosition)
                    PodcastFragment.songPosition = PodcastFragment.musicListPA.size - 1
                else --PodcastFragment.songPosition
            }
        }
    }

    fun songArt(path: String, context: Context): Bitmap {
        val retriever = MediaMetadataRetriever()
        val inputStream: InputStream
        retriever.setDataSource(path)
        if (retriever.embeddedPicture != null) {
            inputStream = ByteArrayInputStream(retriever.embeddedPicture)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            retriever.release()
            return bitmap
        } else {
            return getLargeIcon(context)
        }
    }

    private fun getLargeIcon(context: Context): Bitmap {
        return BitmapFactory.decodeResource(context.resources, R.drawable.music)
    }
}