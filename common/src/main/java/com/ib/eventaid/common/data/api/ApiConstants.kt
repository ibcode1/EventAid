package com.ib.eventaid.common.data.api

object ApiConstants {
    const val BASE_ENDPOINT = "https://api.seatgeek.com/2/"
    const val AUTH_ENDPOINT = "oauth/access_token"
    const val EVENTS_ENDPOINT = "events"
    const val PERFORMERS_ENDPOINT = "performers"
    const val SCOPES ="offline_access"
    const val AUTHORIZATION_ENDPOINT="https://seatgeek.com/oauth2?scope=${SCOPES}&client_id=MzA0ODQyMzR8MTY2ODc5NDg4NC4yNzI4MDE5"

    //const val SECRET ="c6523ebf2071a1184a3bf549217fc62025874a41e7a7a3b980285ce3545ad476"
    //const val CLIENT = "MzA0ODQyMzR8MTY3MDAxNzM1Ny4zMjE3NDg1"
    const val CLIENT = "MzA0ODQyMzR8MTY2ODc5NDg4NC4yNzI4MDE5"
    const val SECRET = "df5e60b6ca949f2b631ac370efee8b264462944d53f40445aa202d2471286074"
}

object ApiParameters {
    const val CODE = "code"
    const val CODE_KEY = ""
    const val REDIRECT_URI = "redirect_uri"
    const val URI = "https://eventing/oauth/fin"
    const val AUTH_HEADER = "Authorization"
    const val GRANT_TYPE_KEY = "grant_type"
    const val GRANT_TYPE = "authorization_code"
    const val CLIENT_ID = "client_id"
    const val CLIENT_SECRET = "client_secret"
    const val PAGE = "page"
    const val PER_PAGE = "per_page"
    const val POSTAL_CODE = "postal_code"
    const val RANGE = "range"
    const val TITLE = "q"
    const val VENUE = "venues"
    const val TYPE = "type"
    const val TAXONOMIES = "taxonomies"
    const val PERFORMER = "performers.id"
    const val SCOPE ="scope"


    const val CONTENT_TYPE ="Content-Type"
    const val CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded; charset=utf-8"
}
