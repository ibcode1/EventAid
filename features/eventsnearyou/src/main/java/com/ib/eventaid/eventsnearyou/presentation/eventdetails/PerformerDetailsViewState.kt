package com.ib.eventaid.eventsnearyou.presentation.eventdetails

import com.ib.eventaid.common.presentation.UIEventPerformer

sealed class PerformerDetailsViewState{
    object Loading:PerformerDetailsViewState()
       data class PerformerDetails(
           val performer:UIEventPerformer
       ):PerformerDetailsViewState()
}

