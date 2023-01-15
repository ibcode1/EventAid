package com.ib.eventaid.common.data.api.model.mappers

import com.ib.eventaid.common.data.api.model.ApiTaxonomy
import com.ib.eventaid.common.domain.model.taxonomy.Taxonomy
import javax.inject.Inject

class ApiTaxonomyMapper @Inject constructor() : ApiMapper<ApiTaxonomy?, Taxonomy> {
    override fun mapToDomain(apiEntity: ApiTaxonomy?): Taxonomy {
        return Taxonomy(
            id = apiEntity?.id?:0,
            name = apiEntity?.name.orEmpty(),
            rank = apiEntity?.rank?:0
        )
    }
}