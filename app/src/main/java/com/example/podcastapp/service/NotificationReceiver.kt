package com.example.podcastapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.podcastapp.R
import com.example.podcastapp.screens.podcast_detail.PodcastFragment
import kotlin.system.exitProcess

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            ApplicationClass.PLAY -> if(PodcastFragment.isPlaying) pauseMusic() else playMusic()
            ApplicationClass.EXIT ->{
                exitApplication()
            }
        }
    }

    private fun playMusic(){
        PodcastFragment.isPlaying = true
        PodcastFragment.musicService!!.mediaPlayer!!.start()
        PodcastFragment.musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_outline_24)
        PodcastFragment.getBindingClass.playPauseBtnPA.setIconResource(R.drawable.ic_baseline_pause_circle_outline_24)
    }

    private fun pauseMusic(){
        PodcastFragment.isPlaying = false
        PodcastFragment.musicService!!.mediaPlayer!!.pause()
        PodcastFragment.musicService!!.showNotification(R.drawable.ic_baseline_play_circle_outline_24)
        PodcastFragment.getBindingClass.playPauseBtnPA.setIconResource(R.drawable.ic_baseline_play_circle_outline_24)
    }

    fun exitApplication(){
        if(PodcastFragment.musicService != null){
//            PodcastFragment.musicService!!.audioManager.abandonAudioFocus(PodcastFragment.musicService)
            PodcastFragment.musicService!!.stopForeground(true)
            PodcastFragment.musicService!!.mediaPlayer!!.release()
            PodcastFragment.musicService = null}
        exitProcess(1)
    }
}