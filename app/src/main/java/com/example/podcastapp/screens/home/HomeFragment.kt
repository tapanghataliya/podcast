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

        viewModel.songResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    getBindingClass().progressBar.isVisible = it.isLoading
                }

                is NetworkResult.Failure -> {
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                    getBindingClass().progressBar.isVisible = false
                }

                is NetworkResult.Success -> {
                    songAdapter.updateSongs(it.data)
                    getBindingClass().progressBar.isVisible = false
                }
            }
        }

        songAdapter.setItemClick(object : ClickInterface<Song> {
            override fun onClick(song: Song) {
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