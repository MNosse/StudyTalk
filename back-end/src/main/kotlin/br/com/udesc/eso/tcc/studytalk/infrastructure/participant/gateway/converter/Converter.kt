package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.gateway.converter

import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema

fun convert(participantSchema: ParticipantSchema): Participant {
    return Participant(
        id = participantSchema.id,
        uid = participantSchema.uid,
        name = participantSchema.name,
        privilege = participantSchema.privilege,
        institution = convert(participantSchema.institution),
        favoriteQuestions = participantSchema.favoriteQuestions.map {
            convert(it)
        }.toMutableList(),
        likedAnswers = participantSchema.likedAnswers.map {
            convert(it)
        }.toMutableList()

    )
}

fun convert(institutionSchema: InstitutionSchema?): Institution? {
    return institutionSchema?.let {
        Institution(
            id = institutionSchema.id,
            registrationCode = institutionSchema.registrationCode,
            name = institutionSchema.name
        )
    }
}

fun convert(questionSchema: QuestionSchema): Question {
    return Question(
        id = questionSchema.id,
        title = questionSchema.title,
        description = questionSchema.description,
        subjects = questionSchema.subjects.toMutableList()
    )
}

fun convert(answerSchema: AnswerSchema): Answer {
    return Answer(
        id = answerSchema.id,
        description = answerSchema.description,
        likeCount = answerSchema.likeCount
    )
}