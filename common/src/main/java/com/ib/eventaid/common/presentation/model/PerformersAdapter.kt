package com.ib.eventaid.common.presentation.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ib.eventaid.common.databinding.RecyclerViewPerformerItemBinding
import com.ib.eventaid.common.presentation.UIPerformer
import com.ib.eventaid.common.utils.setImage


class PerformersAdapter : ListAdapter<UIPerformer, PerformersAdapter.PerformersViewHolder>(ITEM_COMPARATOR) {

    private var performerClickListener: PerformerClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformersViewHolder {
        val binding = RecyclerViewPerformerItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return PerformersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerformersViewHolder, position: Int) {
        val item: UIPerformer = getItem(position)

        holder.bind(item)
    }

    inner class PerformersViewHolder(
        private val binding: RecyclerViewPerformerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UIPerformer) {
            binding.name.text = item.name
            binding.image.setImage(item.image)
            //binding.numUpcomingEvents.text = item.numUpcomingEvents.toString()

            binding.root.setOnClickListener{
                performerClickListener?.onPerformerClicked(item.id)
            }
        }
    }

    fun setOnPerformersClickListener(performerClickListener: PerformerClickListener){
        this.performerClickListener = performerClickListener
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<UIPerformer>() {
    override fun areItemsTheSame(oldItem: UIPerformer, newItem: UIPerformer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UIPerformer, newItem: UIPerformer): Boolean {
        return oldItem == newItem
    }
}

fun interface PerformerClickListener {
    fun onPerformerClicked(performerId: Int)
}