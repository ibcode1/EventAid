package com.ib.eventaid.common.presentation.mappers


import com.ib.eventaid.common.domain.model.performer.Performer
import com.ib.eventaid.common.presentation.UIPerformer
import javax.inject.Inject

class UiPerformerMapper @Inject constructor():UiMapper<Performer, UIPerformer> {
    override fun mapToView(input: Performer): UIPerformer {
        return UIPerformer(
            id = input.id,
            name = input.name,
            image = input.image
        )
    }
}
