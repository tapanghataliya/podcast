package com.example.podcastapp.screens.song

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.SurfaceControl
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.podcastapp.BR
import com.example.podcastapp.R
import com.example.podcastapp.base.BaseFragment
import com.example.podcastapp.data.DataList
import com.example.podcastapp.databinding.FragmentSongBinding
import com.example.podcastapp.service.ForeGroundPlayService
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SongFragment : BaseFragment<FragmentSongBinding, SongViewModel>() {

    private lateinit var songId: String
    private lateinit var link: String
    private lateinit var audioURL: String
    private lateinit var title: String
    private lateinit var subTitle: String
    private var imageUrl: String = ""
    private var mPlayer: SimpleExoPlayer? = null
    var songList : ArrayList<DataList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SongViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments
        if (arguments != null) {
            songId = arguments.getString("id").toString()
            link = arguments.getString("link").toString()
            audioURL = arguments.getString("audioURL").toString()
            imageUrl = arguments.getString("imageURL").toString()
            title = arguments.getString("title").toString()
            subTitle = arguments.getString("type").toString()
            val songLists = arguments.getSerializable("list")

            val transactionList: ArrayList<DataList>? =
                arguments.getSerializable("list") as ArrayList<DataList>?

            Log.d("SONG_LIST", songLists.toString())
            Log.d("songLIST", transactionList.toString())

            getBindingClass().txtSubtitle.text =
                Editable.Factory.getInstance().newEditable(title)
            getBindingClass().txtSubtitle.setSelected(true)
            if (imageUrl.isEmpty()) {
                getBindingClass().imgSong.setImageResource(R.drawable.music)
            } else {
                Glide.with(this)
                    .load(imageUrl)
                    .into(getBindingClass().imgSong)
            }
        }
        context?.let { ForeGroundPlayService.onStartService(it, "bundle") }
        clickHandler()
        sendDataToService()
    }

    private fun sendDataToService() {
        val sendIntent = Intent(context, ForeGroundPlayService::class.java)
        sendIntent.putExtra("audioURL", audioURL)
        sendIntent.putExtra("title", title)
        sendIntent.putExtra("subTitle", subTitle)
        sendIntent.putExtra("imageURL", imageUrl)
        context?.startService(sendIntent)
    }

    private fun clickHandler() {

        @Suppress("ObjectLiteralToLambda")
        getBindingClass().ivBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // TODO fix this warning
                activity?.onBackPressed()
            }
        })

    }

    private fun initPlayer() {
        mPlayer = context?.let { SimpleExoPlayer.Builder(it).build() }
        getBindingClass().playerView.player = mPlayer
        mPlayer!!.playWhenReady = true
        mPlayer!!.setMediaSource(buildMediaSource())
        mPlayer!!.prepare()
    }

    override fun onStart() {
        super.onStart()
        initPlayer()
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || mPlayer == null) {
            initPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (mPlayer == null) {
            return
        }
        mPlayer!!.release()
        mPlayer = null
    }

    private fun buildMediaSource(): MediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(audioURL))
        return mediaSource
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getBindingClass(): FragmentSongBinding {
        return (getViewDataBinding() as FragmentSongBinding)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_song
    }
}