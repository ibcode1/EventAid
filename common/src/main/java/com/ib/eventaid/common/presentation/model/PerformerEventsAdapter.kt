package com.ib.eventaid.common.presentation.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ib.eventaid.common.databinding.RecyclerViewPerformerEventsItemBinding
import com.ib.eventaid.common.presentation.UIPerformerEvents
import com.ib.eventaid.common.utils.setImage

class PerformerEventsAdapter : ListAdapter<UIPerformerEvents, PerformerEventsAdapter.PerformerEventsViewHolder>(ITEM_COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerEventsViewHolder {
        val binding = RecyclerViewPerformerEventsItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return PerformerEventsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerformerEventsViewHolder, position: Int) {
        val item: UIPerformerEvents = getItem(position)
        holder.bind(item)
    }

    inner class PerformerEventsViewHolder(
        private val binding: RecyclerViewPerformerEventsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UIPerformerEvents) {
            binding.image.setImage(item.image)
            binding.title.text = item.title

//            binding.root.setOnClickListener{
//                eventClickListener?.onEventClicked(item.id)
//            }
        }
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<UIPerformerEvents>() {
    override fun areItemsTheSame(oldItem: UIPerformerEvents, newItem: UIPerformerEvents): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UIPerformerEvents, newItem: UIPerformerEvents): Boolean {
        return oldItem == newItem
    }
}
