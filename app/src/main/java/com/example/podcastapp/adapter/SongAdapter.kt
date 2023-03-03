package com.example.podcastapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.podcastapp.R
import com.example.podcastapp.data.Song
import com.example.podcastapp.databinding.ItemPodcastBinding

class SongAdapter : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var songs = mutableListOf<Song>()
    private var clickInterface: ClickInterface<Song>? = null

    fun updateSongs(songs: List<Song>) {
        this.songs = songs.toMutableList()
        notifyItemRangeInserted(0, songs.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemPodcastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.view.txtTitle.text = song.data.title
        holder.view.txtSubtitle.text = song.type
        Glide
            .with(holder.view.imagePodcast.context)
            .load(song.data.image)
            .centerCrop()
            .placeholder(R.drawable.music)
            .into(holder.view.imagePodcast)

        holder.itemView.setOnClickListener {
            clickInterface?.onClick(song)
        }
    }

    fun setItemClick(clickInterface: ClickInterface<Song>) {
        this.clickInterface = clickInterface
    }

    override fun getItemCount(): Int {
        return songs.size

    }

    class SongViewHolder(val view: ItemPodcastBinding) : RecyclerView.ViewHolder(view.root)

}

interface ClickInterface<T> {
    fun onClick(data: T)
}