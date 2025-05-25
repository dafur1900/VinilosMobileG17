package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.models.Artist
import com.example.vinilos.databinding.ArtistItemBinding

class ArtistAdapter(private val onClick: (Artist) -> Unit) :
    ListAdapter<Artist, ArtistAdapter.ArtistViewHolder>(ArtistDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        return ArtistViewHolder(
            ArtistItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)
        holder.bind(artist, onClick)
    }

    class ArtistViewHolder(private val binding: ArtistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist, onClick: (Artist) -> Unit) {
            binding.artist = artist

            Glide.with(binding.root.context)
                .load(artist.image)
                .placeholder(R.drawable.album_placeholder)
                .error(R.drawable.album_placeholder)
                .into(binding.ivArtistImage)

            binding.root.setOnClickListener {
                onClick(artist)
            }

            binding.executePendingBindings()

        }
    }

    companion object {
        private val ArtistDiffCallback = object : DiffUtil.ItemCallback<Artist>() {

            override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem == newItem
            }
        }
    }
}
