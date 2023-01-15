package com.ib.eventaid.common.presentation.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ib.eventaid.common.databinding.RecyclerViewEventPerformerItemBinding
import com.ib.eventaid.common.presentation.UIEventPerformer

class EventPerformerAdapter : ListAdapter<UIEventPerformer,
        EventPerformerAdapter.EventPerformerViewHolder>(ITEM_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventPerformerViewHolder {
        val binding = RecyclerViewEventPerformerItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return EventPerformerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventPerformerViewHolder, position: Int) {
        val item: UIEventPerformer = getItem(position)
        holder.bind(item)
    }

    inner class EventPerformerViewHolder(
        private val binding: RecyclerViewEventPerformerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UIEventPerformer) {
            binding.titleView.text = item.name
        }
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<UIEventPerformer>() {
    override fun areItemsTheSame(
        oldItem: UIEventPerformer,
        newItem: UIEventPerformer
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UIEventPerformer,
        newItem: UIEventPerformer
    ): Boolean {
        return oldItem == newItem
    }
}
