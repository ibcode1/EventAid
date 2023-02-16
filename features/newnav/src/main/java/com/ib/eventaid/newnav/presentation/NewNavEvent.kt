package com.ib.eventaid.newnav.presentation

sealed class NewNavEvent {
    data class PostcodeChanged(val newPostcode: String) : NewNavEvent()
    data class DistanceChanged(val newDistance: String) : NewNavEvent()
    object SubmitButtonClicked : NewNavEvent()
}
