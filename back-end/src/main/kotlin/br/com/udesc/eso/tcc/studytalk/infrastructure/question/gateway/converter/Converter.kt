package br.com.udesc.eso.tcc.studytalk.infrastructure.question.gateway.converter

import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.institution.InstitutionSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.participant.ParticipantSchema
import br.com.udesc.eso.tcc.studytalk.infrastructure.config.db.schema.question.QuestionSchema

fun convert(questionSchema: QuestionSchema): Question {
    return Question(
        id = questionSchema.id,
        title = questionSchema.title,
        description = questionSchema.description,
        subjects = questionSchema.subjects.toMutableList(),
        participant = convert(questionSchema.participant),
        institution = convert(questionSchema.institution)
    )
}

private fun convert(institutionSchema: InstitutionSchema): Institution {
    return Institution(
        id = institutionSchema.id,
        registrationCode = institutionSchema.registrationCode,
        name = institutionSchema.name
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