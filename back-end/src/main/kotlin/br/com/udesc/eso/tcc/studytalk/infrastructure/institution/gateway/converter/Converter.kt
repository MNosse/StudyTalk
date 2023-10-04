package br.com.udesc.eso.tcc.studytalk.infrastructure.institution.gateway.converter

import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema

fun convert(institutionSchema: InstitutionSchema): Institution {
    return Institution(
        id = institutionSchema.id,
        registrationCode = institutionSchema.registrationCode,
        name = institutionSchema.name
    )
}