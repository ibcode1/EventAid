package com.ib.eventaid.common.domain.model.performer

data class Performer(
    val id:Int,
    val name:String,
    val awayTeam:Boolean,
    val hasUpcomingEvents:Boolean,
    val homeTeam:Boolean,
    val image:String,
    val url:String,
    val numUpcomingEvents:Int,
    val score:Double,
    val popularity:Int,
    val slug:String,
    val type:String,
    //val media: Media.Image
    //val taxonomies:List<Taxonomy>
){
}

