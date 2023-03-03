package com.example.podcastapp.screens.song

import android.media.MediaMetadataRetriever
import com.example.podcastapp.base.BaseNavigator
import com.example.podcastapp.base.BaseViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor() : BaseViewModel<BaseNavigator>() {

    var mPlayer: SimpleExoPlayer? = null

    fun stopPlayer() {
        if (mPlayer == null) {
            return
        }
        mPlayer!!.release()
        mPlayer = null
    }
}