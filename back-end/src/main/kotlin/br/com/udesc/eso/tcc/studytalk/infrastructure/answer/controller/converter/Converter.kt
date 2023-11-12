package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.converter

import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response.Participant
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response.Question

fun convert(questions: MutableList<br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer>): MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response.Response> {
    return questions.map {
        convert(it)
    }.toMutableList()
}

fun convert(answer: br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer): br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response.Response {
    return br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response.Response(
        id = answer.id,
        description = answer.description,
        likeCount = answer.likeCount,
        question = convert(answer.question),
        participant = convert(answer.participant)
    )
}

fun convert(question: br.com.udesc.eso.tcc.studytalk.entity.question.model.Question?): Question? {
    return question?.let {
        Question(
            id = question.id,
            title = question.title,
            description = question.description,
            subjects = question.subjects
        )
    }
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