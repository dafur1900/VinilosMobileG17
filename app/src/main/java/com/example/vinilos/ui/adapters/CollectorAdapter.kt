package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.vinilos.data.models.Collector
import com.example.vinilos.databinding.ListItemCollectorBinding

class CollectorAdapter(private val onCollectorClick: (Collector) -> Unit) :
    ListAdapter<Collector, CollectorAdapter.CollectorViewHolder>(CollectorDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        return CollectorViewHolder(
            ListItemCollectorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        val collector = getItem(position)
        holder.bind(collector)
        holder.binding.root.setOnClickListener {
            onCollectorClick(collector)
        }
    }

    class CollectorViewHolder(val binding: ListItemCollectorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(collector: Collector) {
            binding.collector = collector
            binding.executePendingBindings()
        }
    }

    companion object {
        private val CollectorDiffCallback = object : DiffUtil.ItemCallback<Collector>() {

            override fun areItemsTheSame(oldItem: Collector, newItem: Collector): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Collector, newItem: Collector): Boolean {
                return oldItem == newItem
            }
        }
    }
}
