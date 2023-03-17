package com.example.podcastapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.podcastapp.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MusicService: Service(), MediaPlayer.OnPreparedListener {

    private val CHANNEL_ID = "ForegroundService Kotlin"
    private val NOTIFICATION_ID = 1

    private var notificationManager: NotificationManager? = null

    private var audio: String = ""
    private lateinit var title: String
    private lateinit var publisher: String
    private var imageUrl: String = ""

    var mediaPlayer: MediaPlayer? = null
    private var myBinder = MyBinder()
    private var currentPosition: Int = 0

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        title = intent!!.getStringExtra("title").toString()
        publisher = intent.getStringExtra("publisher").toString()
        imageUrl = intent.getStringExtra("thumbnail").toString()
        audio = intent.getStringExtra("audioURL").toString()

        intent?.let {
            when (it.action) {
                "PLAY" -> {
                    playSong()
                }
                "PAUSE" -> {
                    pauseSong()
                }
                else -> {
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(audio)
                        setOnPreparedListener(this@MusicService)
                        prepareAsync()
                    }
                }
            }
        }
        createNotification(title, publisher, imageUrl)

        return START_NOT_STICKY

    }

    private fun createNotification(
        title: String, publisher: String, imageUrl: String
    ) {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else -> PendingIntent.FLAG_UPDATE_CURRENT
        }

        val playIntent = Intent(this, MusicService::class.java)
        playIntent.action = "PLAY"
        val playPendingIntent: PendingIntent =
            PendingIntent.getService(this, 0, playIntent, flags)

        val pauseIntent = Intent(this, MusicService::class.java)
        pauseIntent.action = "PAUSE"
        val pausePendingIntent: PendingIntent =
            PendingIntent.getService(this, 0, pauseIntent, flags)


        Glide.with(this).asBitmap().load(imageUrl).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap, transition: Transition<in Bitmap>?
            ) {

                val notification = NotificationCompat.Builder(this@MusicService, CHANNEL_ID)
                    .setContentTitle(title).setContentText(publisher)
                    .setSmallIcon(R.drawable.music)
                    .setLargeIcon(resource)
                    .setAutoCancel(true)
//                    .addAction(
//                        if (mediaPlayer!!.isPlaying) R.drawable.ic_baseline_pause_circle_outline_24 else R.drawable.ic_baseline_play_circle_outline_24,
//                        if (mediaPlayer!!.isPlaying) "PAUSE" else "PLAY",
//                        pausePendingIntent
//                    )
                    .addAction(
                        R.drawable.ic_baseline_play_circle_outline_24,
                        "Play",
                        playPendingIntent
                    )
                    .addAction(
                        R.drawable.ic_baseline_pause_circle_outline_24,
                        "Stop",
                        pausePendingIntent
                    )
                    .setStyle(
                        androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1).setShowCancelButton(true)
//                                .setCancelButtonIntent(stopPendingIntent)
                    ).build()

                notificationManager!!.notify(NOTIFICATION_ID, notification)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })

    }

    fun playSong() {
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            mediaPlayer!!.start()
        }
    }

    fun pauseSong() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
    }

    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.e("ClearService", "END")
        stopSelf()
    }

    fun forwardSeconds() {
        mediaPlayer?.let {
            currentPosition = mediaPlayer!!.currentPosition
            currentPosition += 10000
            mediaPlayer!!.seekTo(currentPosition)
        }
    }

    fun backwordSeconds() {
        mediaPlayer?.let {
            currentPosition = mediaPlayer!!.currentPosition
            currentPosition -= 10000
            mediaPlayer!!.seekTo(currentPosition)
        }
    }
}