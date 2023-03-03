package com.example.podcastapp.screens.podcast_detail

import android.media.MediaPlayer
import com.example.podcastapp.base.BaseNavigator
import com.example.podcastapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PodcastViewModel @Inject constructor(): BaseViewModel<BaseNavigator>() {
    lateinit var mediaPlayer: MediaPlayer

    fun playSong(){
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    fun pauseSong(){
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.release()
    }
}