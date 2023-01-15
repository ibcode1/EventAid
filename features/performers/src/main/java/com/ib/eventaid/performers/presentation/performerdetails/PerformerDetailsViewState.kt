package com.ib.eventaid.performers.presentation.performerdetails

import com.ib.eventaid.performers.presentation.performerdetails.model.UIPerformerDetailed

sealed class PerformerDetailsViewState {
    object Loading : PerformerDetailsViewState()

    data class PerformerDetails(
        val performer:UIPerformerDetailed
    ):PerformerDetailsViewState()

    object Failure : PerformerDetailsViewState()
}