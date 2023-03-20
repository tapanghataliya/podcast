package com.example.podcastapp.screens.podcast_detail

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.podcastapp.BR
import com.example.podcastapp.R
import com.example.podcastapp.base.BaseFragment
import com.example.podcastapp.databinding.FragmentPodcastBinding
import com.example.podcastapp.service.MusicService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PodcastFragment : BaseFragment<FragmentPodcastBinding, PodcastViewModel>(),
    SeekBar.OnSeekBarChangeListener {

    private var audio: String = ""
    private lateinit var title: String
    private lateinit var titleDetail: String
    private lateinit var publisher: String
    private var imageUrl: String = ""
    private lateinit var myService: MusicService

    private var isBound = false

    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private lateinit var musicServiceIntent: Intent
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            myService = (service as MusicService.MyBinder).currentService()
            getBindingClass().progressBar.visibility = View.GONE
            val musicBinder = service as MusicService.MyBinder
            myService = musicBinder.currentService()
            isBound = true
            initializeSeekBar()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PodcastViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        val arguments = arguments
        if (arguments != null) {
            title = arguments.getString("title").toString()
            titleDetail = arguments.getString("titleDetail").toString()
            publisher = arguments.getString("publisher").toString()
            imageUrl = arguments.getString("thumbnail").toString()
            audio = arguments.getString("audio").toString()
            var objectDatas = arguments.getParcelableArray("data")
            Log.d("objectDatas", objectDatas.toString())

            getBindingClass().songNamePA.text = title
            getBindingClass().songDetail.text = titleDetail
            getBindingClass().songDetail.setSelected(true)
            getBindingClass().songType.text = publisher

            Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions().placeholder(R.drawable.music).centerCrop())
                .into(getBindingClass().imgSong)
        }

        musicServiceIntent = Intent(context, MusicService::class.java)
        activity?.bindService(musicServiceIntent, connection, AppCompatActivity.BIND_AUTO_CREATE)
        musicServiceIntent.putExtra("audioURL", audio)
        musicServiceIntent.putExtra("title", title)
        musicServiceIntent.putExtra("publisher", publisher)
        musicServiceIntent.putExtra("thumbnail", imageUrl)
        activity?.startService(musicServiceIntent)

        clickHandler()
        getBindingClass().seekBarPA.setOnSeekBarChangeListener(this@PodcastFragment)
    }

    private fun clickHandler() {
        getBindingClass().backBtnPA.setOnClickListener {
            activity?.onBackPressed()
        }

        getBindingClass().playPauseBtnPA.setOnClickListener {
            myService.pauseSong()
            initializeSeekBar()
            getBindingClass().playBtnPA.visibility = View.VISIBLE
            getBindingClass().playPauseBtnPA.visibility = View.GONE
        }

        getBindingClass().playBtnPA.setOnClickListener {
            myService.playSong()
            initializeSeekBar()
            getBindingClass().playPauseBtnPA.visibility = View.VISIBLE
            getBindingClass().playBtnPA.visibility = View.GONE
        }

        getBindingClass().previousBtnPA.setOnClickListener {
            myService.backwordSeconds()
            initializeSeekBar()
        }

        getBindingClass().nextBtnPA.setOnClickListener {
            myService.forwardSeconds()
            initializeSeekBar()
        }
    }

    private fun initializeSeekBar() {
        getBindingClass().seekBarPA.max = myService.mediaPlayer!!.seconds
        runnable = Runnable {
            getBindingClass().seekBarPA.progress = myService.mediaPlayer!!.currentSeconds
            getBindingClass().tvSeekBarStart.text = "${myService.mediaPlayer!!.currentSeconds} sec"
            val diff = myService.mediaPlayer!!.seconds - myService.mediaPlayer!!.currentSeconds
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
            myService.mediaPlayer!!.seekTo(progress * 1000)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}
