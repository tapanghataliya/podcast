package com.example.podcastapp.service

import android.app.*
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.podcastapp.R
import com.example.podcastapp.screens.MainActivity
import com.example.podcastapp.screens.podcast_detail.PodcastFragment
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForeGroundPlayService : Service() {

    private val CHANNEL_ID = "ForegroundService Kotlin"
    var mPlayer: SimpleExoPlayer? = null
    private lateinit var audioURL: String
    private lateinit var mediaSession: MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return null
    }

    companion object {
        fun onStartService(context: Context, message: String) {
            val startIntent = Intent(context, ForeGroundPlayService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun onStopService(context: Context) {
            val stopIntent = Intent(context, ForeGroundPlayService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        audioURL = intent!!.getStringExtra("audioURL").toString()
        val title = intent.getStringExtra("title")
        val subTitle = intent.getStringExtra("subTitle")
        val imageURL = intent.getStringExtra("imageURL")

        Log.d("audioURL", audioURL)

        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            else -> FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, flags
        )

        val intent = Intent(this, ForeGroundPlayService::class.java)
        val exitPendingIntent = getBroadcast(
            this, 0, intent,
            FLAG_MUTABLE + FLAG_CANCEL_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.music)
            .setContentTitle(title)
            .setContentText(subTitle)
            .setSound(null)
            .setShowWhen(false)
            .setColorized(true)
            .setColor(Color.BLACK)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .addAction(
                R.drawable.ic_baseline_keyboard_double_arrow_left_24,
                "Previous",
                pendingIntent
            )
            .addAction(R.drawable.ic_baseline_play_circle_outline_24, "Play", pendingIntent)
            .addAction(R.drawable.ic_baseline_keyboard_double_arrow_right_24, "Next", pendingIntent)
            .addAction(R.drawable.ic_baseline_cancel_24, "exit", exitPendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer!!.stop()
        mPlayer!!.release()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}
