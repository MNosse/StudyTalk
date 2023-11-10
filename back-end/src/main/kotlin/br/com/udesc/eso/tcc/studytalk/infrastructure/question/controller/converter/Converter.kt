package br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.converter

import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Institution
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Participant

fun convert(questions: MutableList<br.com.udesc.eso.tcc.studytalk.entity.question.model.Question>): MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Response> {
    return questions.map {
        convert(it)
    }.toMutableList()
}

fun convert(question: br.com.udesc.eso.tcc.studytalk.entity.question.model.Question): br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Response {
    return br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Response(
        id = question.id,
        title = question.title,
        description = question.description,
        subjects = question.subjects,
        participant = convert(question.participant),
        institution = convert(question.institution)
    )
}

fun convert(participant: br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant?): Participant? {
    return participant?.let {
        Participant(
            id = participant.id,
            uid = participant.uid,
            name = participant.name,
            privilege = participant.privilege
        )
    }
}

private fun convert(institution: br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution?): Institution? {
    return institution?.let {
        Institution(
            id = institution.id,
            registrationCode = institution.registrationCode!!,
            name = institution.name
        )
    }
}