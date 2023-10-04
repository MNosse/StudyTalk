package br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.gateway.converter

import br.com.udesc.eso.tcc.studytalk.entity.administrator.model.Administrator
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.administrator.AdministratorSchema

fun convert(administratorSchema: AdministratorSchema): Administrator {
    return Administrator(
        id = administratorSchema.id,
        uid = administratorSchema.uid
    )
}