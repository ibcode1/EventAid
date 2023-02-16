package com.ib.eventaid.newnav.presentation

sealed class NewNavViewEffect {
    object NavigateToEventsNearYou:NewNavViewEffect()
}