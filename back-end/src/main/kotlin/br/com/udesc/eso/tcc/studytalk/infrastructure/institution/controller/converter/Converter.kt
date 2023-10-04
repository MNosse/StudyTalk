package br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.converter

fun convert(institutions: MutableList<br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution>): MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.response.Response> {
    return institutions.map {
        convert(it)
    }.toMutableList()
}

fun convert(institution: br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution): br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.response.Response {
    return br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.response.Response(
        id = institution.id,
        registrationCode = institution.registrationCode!!,
        name = institution.name
    )
}