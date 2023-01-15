package com.ib.eventaid.common.data.preferences


interface Preferences {
    fun putToken(token: String)

    fun putTokenExpirationTime(time: Long)

    fun putTokenType(tokenType: Int)

    fun getToken(): String

    fun getTokenExpirationTime(): Long

    fun getTokenType(): String

    fun deleteTokenInfo()


    ////////////////////////////

    fun getPostcode(): String

    fun putPostcode(postcode: String)

    fun getMaxDistanceAllowedToGetEvents(): String

    fun putMaxDistanceAllowedToGetEvents(distance: String)
}
