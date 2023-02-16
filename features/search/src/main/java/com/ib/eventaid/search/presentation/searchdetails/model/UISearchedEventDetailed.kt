package com.ib.eventaid.search.presentation.searchdetails.model

import com.ib.eventaid.common.domain.model.performer.Performer
import java.time.LocalDateTime

data class UISearchedEventDetailed (
        val id: Int,
        val title: String,
        val image: String,
        val description: String,
        val performer: List<Performer>,
        val date: LocalDateTime,
        val type: List<String>,
        val city: String,
        val price:String,
        val venue: String,
        val capacity: Int,
        val score: Double,
        val address: String,
        val taxonomy: List<String>
)