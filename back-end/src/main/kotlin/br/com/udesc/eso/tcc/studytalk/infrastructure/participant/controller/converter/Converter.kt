package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.converter

import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.response.Answer
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.response.Institution
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.response.Question
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
        institution = convert(participant.institution),
        favoriteQuestions = participant.favoriteQuestions.map {
            convert(it)
        }.toMutableList(),
        likedAnswers = participant.likedAnswers.map {
            convert(it)
        }.toMutableList()
    )
}

fun convert(answer: br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer): Answer {
    return Answer(
        id = answer.id,
        description = answer.description,
        likeCount = answer.likeCount
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

fun convert(question: br.com.udesc.eso.tcc.studytalk.entity.question.model.Question): Question {
    return Question(
        id = question.id,
        title = question.title,
        description = question.description,
        subjects = question.subjects
    )
}