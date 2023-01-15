package com.ib.eventaid.common.presentation.mappers

import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.presentation.UIEventPerformer
import javax.inject.Inject

class UiEventPerformerMapper @Inject constructor():UiMapper<Performer, UIEventPerformer>{
    override fun mapToView(input: Performer): UIEventPerformer {
        return UIEventPerformer(
            id = input.id,
            name = input.name
        )
    }
}