package com.ib.eventaid.common.domain.model.pagination

data class Pagination(
    val page:Int, //ie the current page
    val totalPages:Int,
    //val geolocation: GeoLocation
){
    companion object{
        const val UNKNOWN_TOTAL = -1
        const val DEFAULT_PAGE_SIZE = 20
    }

    val canLoadMore:Boolean
    get() = totalPages == UNKNOWN_TOTAL || page < totalPages
}
