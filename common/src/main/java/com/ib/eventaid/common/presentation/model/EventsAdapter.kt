package com.ib.eventaid.common.presentation.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ib.eventaid.common.databinding.RecyclerViewEventItemBinding
import com.ib.eventaid.common.presentation.UIEvent
import com.ib.eventaid.common.utils.setImage


class EventsAdapter : ListAdapter<UIEvent, EventsAdapter.EventsViewHolder>(ITEM_COMPARATOR) {

    private var eventClickListener: EventClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val binding = RecyclerViewEventItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return EventsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val item: UIEvent = getItem(position)
        holder.bind(item)
    }

    inner class EventsViewHolder(
        private val binding: RecyclerViewEventItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UIEvent) {
            binding.title.text = item.title
            binding.image.setImage(item.image)

            binding.root.setOnClickListener{
                eventClickListener?.onEventClicked(item.id)
            }
        }
    }

    fun setOnEventClickListener(eventClickListener:EventClickListener){
        this.eventClickListener = eventClickListener
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<UIEvent>() {
    override fun areItemsTheSame(oldItem: UIEvent, newItem: UIEvent): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UIEvent, newItem: UIEvent): Boolean {
        return oldItem == newItem
    }
}

fun interface EventClickListener {
    fun onEventClicked(eventId: Int)
}