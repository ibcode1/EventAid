package com.ib.eventaid.performers.presentation

sealed class PerformersEvents {
    //  object RequestInitialPerformersList: PerformersEvents()
    object RequestMorePerformers:PerformersEvents()
}