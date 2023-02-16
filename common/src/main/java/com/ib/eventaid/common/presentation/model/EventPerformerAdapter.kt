package com.ib.eventaid.common.presentation.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ib.eventaid.common.databinding.RecyclerViewEventPerformerItemBinding
import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.utils.setImageRound

class EventPerformerAdapter : ListAdapter<Performer, EventPerformerAdapter.EventPerformerViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventPerformerViewHolder {
        val binding = RecyclerViewEventPerformerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return EventPerformerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventPerformerViewHolder, position: Int) {
        val item: Performer = getItem(position)
        holder.bind(item)
    }

    inner class EventPerformerViewHolder(
        private val binding: RecyclerViewEventPerformerItemBinding
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Performer){
            binding.performerName.text = item.name
            binding.numUpcomingEvents.text = item.numUpcomingEvents.toString() + " " + "Upcoming Event"
            binding.performerImage.setImageRound(item.image)
        }
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Performer>(){
    override fun areItemsTheSame(oldItem: Performer, newItem: Performer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Performer, newItem: Performer): Boolean {
        return oldItem == newItem
    }
}