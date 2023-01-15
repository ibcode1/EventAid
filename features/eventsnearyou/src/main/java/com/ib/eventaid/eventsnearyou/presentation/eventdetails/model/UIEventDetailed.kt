package com.ib.eventaid.eventsnearyou.presentation.eventdetails.model

import com.ib.eventaid.common.domain.model.performer.Performer

data class UIEventDetailed(
    val id:Int,
    val title:String,
    val image:String,
    val performers:List<Performer>,
    val description:String,
    val averagePrice: Int,
    val highestPrice: Int,
    val listingCount: Int,
    val lowestPrice: Int,
    val medianPrice: Int,
    //val taxonomy:List<String>
){

}

