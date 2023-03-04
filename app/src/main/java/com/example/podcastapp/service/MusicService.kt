package com.example.podcastapp.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.example.podcastapp.R
import com.example.podcastapp.screens.MainActivity
import com.example.podcastapp.screens.podcast_detail.PodcastFragment
import com.example.podcastapp.utils.getImgArt
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.concurrent.formatDuration


@AndroidEntryPoint
class MusicService : Service() {

    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification(playPauseBtn: Int) {

        val intent = Intent(baseContext, MainActivity::class.java)
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val contentIntent = PendingIntent.getActivity(this, 0, intent, flag)

        val playIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 0, playIntent, flag)

        val exitIntent =
            Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent, flag)

        val imgArt = PodcastFragment.musicListPA[PodcastFragment.songPosition].data.image?.let {
            getImgArt(
                it
            )
        }
        val image = if (imgArt != null) {
            BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.music)
        }

        val notification =
            androidx.core.app.NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setContentTitle("Main Title")
                .setContentText("Song by artist")
                .setSmallIcon(R.drawable.music)
                .setLargeIcon(image)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.sessionToken)
                )
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_baseline_keyboard_double_arrow_right_24, "Previous", null)
                .addAction(playPauseBtn, "Play", playPendingIntent)
                .addAction(R.drawable.ic_baseline_keyboard_double_arrow_left_24, "Next", null)
                .addAction(R.drawable.ic_baseline_cancel_24, "Exit", exitPendingIntent)
                .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val playbackSpeed = if (PodcastFragment.isPlaying) 1F else 0F
            mediaSession.setMetadata(
                MediaMetadataCompat.Builder()
                    .putLong(
                        MediaMetadataCompat.METADATA_KEY_DURATION,
                        mediaPlayer!!.duration.toLong()
                    )
                    .build()
            )
            val playBackState = PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    mediaPlayer!!.currentPosition.toLong(),
                    playbackSpeed
                )
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
        }
        startForeground(11, notification)
    }

    fun createMediaPlayer() {
        try {
            if (mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(PodcastFragment.musicListPA[PodcastFragment.songPosition].data.audio)
            mediaPlayer!!.prepare()
            PodcastFragment.getBindingClass.playPauseBtnPA.setIconResource(R.drawable.ic_baseline_pause_circle_outline_24)
            showNotification(R.drawable.ic_baseline_pause_circle_outline_24)
            PodcastFragment.getBindingClass.tvSeekBarStart.text =
                formatDuration(mediaPlayer!!.currentPosition.toLong())
            PodcastFragment.getBindingClass.tvSeekBarEnd.text =
                formatDuration(mediaPlayer!!.duration.toLong())
            PodcastFragment.getBindingClass.seekBarPA.progress = 0
            PodcastFragment.getBindingClass.seekBarPA.max = mediaPlayer!!.duration
        } catch (e: Exception) {
            return
        }
    }

    fun seekBarSetup() {
//        runnable = Runnable {
//            PodcastFragment.getBindingClass.tvSeekBarStart.text =
//                formatDuration(mediaPlayer!!.currentPosition.toLong())
//            PodcastFragment.getBindingClass.seekBarPA.progress = mediaPlayer!!.currentPosition
//            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
//        }
//        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val title = intent.getStringExtra("data")
            Log.d("OBJECT_LIST", title.toString())
        }
        return START_NOT_STICKY
    }
}