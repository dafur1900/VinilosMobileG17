package com.example.vinilos.ui.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.data.models.Collector
import com.example.vinilos.databinding.ListItemCollectorBinding

class CollectorAdapter(private val onItemClicked: (Collector) -> Unit) :
    ListAdapter<Collector, CollectorAdapter.CollectorViewHolder>(CollectorDiffCallback()) {

    class CollectorViewHolder(val binding: ListItemCollectorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(collector: Collector) {
            binding.textViewCollectorName.text = collector.name
            binding.textViewCollectorEmail.text = collector.email

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCollectorBinding.inflate(inflater, parent, false)
        return CollectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        val collector = getItem(position)
        holder.bind(collector)
        holder.itemView.setOnClickListener {
            onItemClicked(collector)
        }
    }
}

class CollectorDiffCallback : DiffUtil.ItemCallback<Collector>() {
    override fun areItemsTheSame(oldItem: Collector, newItem: Collector): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Collector, newItem: Collector): Boolean {
        return oldItem == newItem
    }
}

