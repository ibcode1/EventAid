package com.ib.eventaid.performers.presentation.performerdetails.model.mapper

import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.presentation.mappers.UiMapper
import com.ib.eventaid.performers.presentation.performerdetails.model.UIPerformerDetailed
import javax.inject.Inject

class UiPerformerDetailsMapper @Inject constructor():UiMapper<Performer,UIPerformerDetailed>{
    override fun mapToView(input: Performer): UIPerformerDetailed {
        return UIPerformerDetailed(
            id = input.id,
            name = input.name,
            numUpcomingEvents = input.numUpcomingEvents,
            image = input.image
        )
    }
}