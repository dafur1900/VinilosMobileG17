package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.models.Artist
import com.example.vinilos.databinding.ArtistAlbumDetailItemBinding

class PerformerAdapter : ListAdapter<Artist, PerformerAdapter.PerformerViewHolder>(PerformerDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
        return PerformerViewHolder(
            ArtistAlbumDetailItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: PerformerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PerformerViewHolder(private val binding: ArtistAlbumDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist) {
            binding.artist = artist
            Glide.with(binding.root.context)
                .load(artist.image)
                .placeholder(R.drawable.album_placeholder)
                .error(R.drawable.album_placeholder)
                .into(binding.ivArtistImage)
            binding.executePendingBindings()

        }
    }
    
    companion object {
        private val PerformerDiffCallback = object : DiffUtil.ItemCallback<Artist>() {
            override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem == newItem
            }
        }
    }
}
