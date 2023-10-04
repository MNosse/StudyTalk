package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.gateway.converter

import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.answer.AnswerSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema

fun convert(answerSchema: AnswerSchema): Answer {
    return Answer(
        id = answerSchema.id,
        description = answerSchema.description,
        likeCount = answerSchema.likeCount,
        participant = convert(answerSchema.participant),
        question = convert(answerSchema.question)
    )
}

private fun convert(institutionSchema: InstitutionSchema): Institution {
    return Institution(
        id = institutionSchema.id,
        name = institutionSchema.name,
        registrationCode = institutionSchema.registrationCode
    )
}

private fun convert(participantSchema: ParticipantSchema): Participant {
    return Participant(
        id = participantSchema.id,
        uid = participantSchema.uid,
        name = participantSchema.name,
        privilege = participantSchema.privilege
    )
}

private fun convert(questionSchema: QuestionSchema): Question {
    return Question(
        id = questionSchema.id,
        title = questionSchema.title,
        description = questionSchema.description,
        subjects = questionSchema.subjects,
        institution = convert(questionSchema.institution)
    )
}