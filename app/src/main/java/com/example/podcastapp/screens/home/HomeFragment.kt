package com.example.podcastapp.screens.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.podcastapp.BR
import com.example.podcastapp.R
import com.example.podcastapp.adapter.ClickInterface
import com.example.podcastapp.adapter.SongAdapter
import com.example.podcastapp.base.BaseFragment
import com.example.podcastapp.data.test.Item
import com.example.podcastapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        songAdapter = SongAdapter()
        getBindingClass().rvPodcast.layoutManager = GridLayoutManager(context, 2)
        getBindingClass().rvPodcast.adapter = songAdapter

        viewModel.users.observe(viewLifecycleOwner) {song->

            getBindingClass().progressBar.visibility = View.GONE
            songAdapter.updateSongs(song.items)
        }

        songAdapter.setItemClick(object : ClickInterface<Item> {
            override fun onClick(song: Item) {
                val bundle = Bundle()
                bundle.putString("title", song.data.podcast.title)
                bundle.putString("titleDetail", song.data.title)
                bundle.putString("publisher", song.data.podcast.publisher)
                bundle.putString("thumbnail", song.data.podcast.thumbnail)
                bundle.putString("audio", song.data.audio)
                findNavController().navigate(R.id.podcastFragment, bundle)
            }
        })
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getBindingClass(): FragmentHomeBinding {
        return (getViewDataBinding() as FragmentHomeBinding)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

}