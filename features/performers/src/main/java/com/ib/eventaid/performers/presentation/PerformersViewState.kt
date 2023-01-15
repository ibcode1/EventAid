package com.ib.eventaid.performers.presentation

import com.ib.eventaid.common.presentation.UIPerformer
import com.ib.eventaid.common.presentation.model.Event

data class PerformersViewState(
    val loading:Boolean = true,
    val performers:List<UIPerformer> = emptyList(),
    val noMorePerformers:Boolean =false,
    val failure: Event<Throwable>? = null
)
