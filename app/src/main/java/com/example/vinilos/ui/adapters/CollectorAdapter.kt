package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        holder.viewDataBinding.collector = collector
        holder.viewDataBinding.root.setOnClickListener {
            onCollectorClick(collector)
        }
    }

    class CollectorViewHolder(val viewDataBinding: ListItemCollectorBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
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
