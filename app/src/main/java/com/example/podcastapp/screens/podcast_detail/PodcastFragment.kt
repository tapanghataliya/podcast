package com.example.podcastapp.screens.podcast_detail

import android.content.ComponentName
import android.content.ServiceConnection
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.podcastapp.BR
import com.example.podcastapp.R
import com.example.podcastapp.base.BaseFragment
import com.example.podcastapp.data.Song
import com.example.podcastapp.databinding.FragmentPodcastBinding
import com.example.podcastapp.service.MusicService
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


@AndroidEntryPoint
class PodcastFragment : BaseFragment<FragmentPodcastBinding, PodcastViewModel>(),
    SeekBar.OnSeekBarChangeListener, ServiceConnection {

    private val args: PodcastFragmentArgs by navArgs()

    companion object {

        lateinit var musicListPA: ArrayList<Song>
        var songPosition: Int = 0
        var isPlaying: Boolean = false
        var musicService: MusicService? = null
        lateinit var getBindingClass: FragmentPodcastBinding
    }

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[PodcastViewModel::class.java]

//        val intentService = Intent(context, MusicService::class.java)
//        activity?.bindService(intentService, this, AppCompatActivity.BIND_AUTO_CREATE)
//        context?.startService(intentService)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()

        getBindingClass().songNamePA.text = args.songList.data.title
        getBindingClass().songNamePA.setSelected(true)
        getBindingClass().songType.text = args.songList.type

        Glide.with(this)
            .load(args.songList.data.image)
            .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
            .into(getBindingClass().imgSong)

        playMusic()
        clickHandler()
        getBindingClass().seekBarPA.setOnSeekBarChangeListener(this@PodcastFragment)
    }

    private fun playMusic() {
        isPlaying = true
        mediaPlayer = MediaPlayer()
        mediaPlayer.seekTo(mediaPlayer.currentPosition)
        getBindingClass().progressBar.visibility = View.VISIBLE
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        getBindingClass().seekBarPA.isClickable = false
        try {
            mediaPlayer.setDataSource(args.songList.data.audio)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener {
                initializeSeekBar()
                mediaPlayer.start()
                getBindingClass().progressBar.visibility = View.GONE
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }
        getBindingClass().playPauseBtnPA.setIconResource(R.drawable.ic_baseline_pause_circle_outline_24)
    }

    private fun pauseMusic() {
        isPlaying = false
        mediaPlayer.seekTo(mediaPlayer.currentPosition)
        mediaPlayer.pause()
        getBindingClass().playPauseBtnPA.setIconResource(R.drawable.ic_baseline_play_circle_outline_24)
    }

    override fun onStart() {
        super.onStart()
        playMusic()
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || mediaPlayer == null) {
            playMusic()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isPlaying = false
        mediaPlayer.pause()
    }

    override fun onStop() {
        super.onStop()
        isPlaying = false
        mediaPlayer.pause()
    }

    private fun clickHandler() {
        getBindingClass().backBtnPA.setOnClickListener {
            activity?.onBackPressed()
        }

        getBindingClass().playPauseBtnPA.setOnClickListener {
            if (isPlaying) pauseMusic() else playMusic()
        }

    }

    private fun initializeSeekBar() {
        getBindingClass().seekBarPA.max = mediaPlayer.seconds
        runnable = Runnable {
            getBindingClass().seekBarPA.progress = mediaPlayer.currentSeconds
            getBindingClass().tvSeekBarStart.text = "${mediaPlayer.currentSeconds} sec"
            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            getBindingClass().tvSeekBarEnd.text = "$diff sec"
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    private val MediaPlayer.seconds: Int
        get() {
            return this.duration / 1000
        }

    private val MediaPlayer.currentSeconds: Int
        get() {
            return this.currentPosition / 1000
        }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getBindingClass(): FragmentPodcastBinding {
        return (getViewDataBinding() as FragmentPodcastBinding)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_podcast
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            mediaPlayer.seekTo(progress * 1000)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        if (musicService == null) {
            val binder = service as MusicService.MyBinder
            musicService = binder.currentService()
        }
        playMusic()
        musicService!!.seekBarSetup()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

}