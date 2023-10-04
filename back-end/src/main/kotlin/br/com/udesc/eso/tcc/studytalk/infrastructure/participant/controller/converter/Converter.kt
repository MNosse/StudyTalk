package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.converter

import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.response.Institution
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.response.Response

fun convert(participants: MutableList<br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant>): MutableList<Response> {
    return participants.map {
        convert(it)
    }.toMutableList()
}

fun convert(participant: br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant): Response {
    return Response(
        id = participant.id,
        uid = participant.uid,
        name = participant.name,
        privilege = participant.privilege,
        institution = convert(participant.institution)
    )
}

private fun convert(institution: br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution?): Institution? {
    return institution?.let {
        return Institution(
            id = institution.id,
            registrationCode = institution.registrationCode!!,
            name = institution.name
        )
    }
}