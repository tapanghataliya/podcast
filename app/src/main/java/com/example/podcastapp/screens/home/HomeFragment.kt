package com.example.podcastapp.screens.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.podcastapp.BR
import com.example.podcastapp.R
import com.example.podcastapp.adapter.ClickInterface
import com.example.podcastapp.adapter.SongAdapter
import com.example.podcastapp.base.BaseFragment
import com.example.podcastapp.data.NetworkResult
import com.example.podcastapp.data.Song
import com.example.podcastapp.data.test.Item
import com.example.podcastapp.data.test.SongList
import com.example.podcastapp.databinding.FragmentHomeBinding
import com.example.podcastapp.utils.Status
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
                val action = HomeFragmentDirections.actionHomeFragmentToPodcastFragment(song)
                findNavController().navigate(action)
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