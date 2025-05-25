package com.example.vinilos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.data.models.Collector
import com.example.vinilos.databinding.ListItemCollectorBinding

class CollectorAdapter(private val onCollectorClick: (Collector) -> Unit) :
    ListAdapter<Collector, CollectorAdapter.CollectorViewHolder>(CollectorDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val binding = ListItemCollectorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CollectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        val collector = getItem(position)
        holder.binding.textViewCollectorName.text = collector.name
        holder.binding.textViewCollectorEmail.text = collector.email
        holder.binding.root.setOnClickListener {
            onCollectorClick(collector)
        }
    }

    class CollectorViewHolder(val binding: ListItemCollectorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_collector
        }
    }

    companion object {
        val CollectorDiffCallback = object : DiffUtil.ItemCallback<Collector>() {
            override fun areItemsTheSame(oldItem: Collector, newItem: Collector): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Collector, newItem: Collector): Boolean {
                return oldItem == newItem
            }
        }
    }
}
