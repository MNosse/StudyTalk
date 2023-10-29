package br.com.udesc.eso.tcc.studytalk.featureQuestion.data.converter

import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.entity.InstitutionRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.entity.ParticipantRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.entity.QuestionRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.model.QuestionApiModel
import br.com.udesc.eso.tcc.studytalk.featureQuestion.domain.model.Question

fun convertToModel(
    question: QuestionRoomEntity
): Question {
    return Question(
        id = question.id,
        title = question.title,
        description = question.description,
        subjects = question.subjects
    )
}

fun convertToModel(
    question: QuestionRoomEntity,
    participant: ParticipantRoomEntity,
    institution: InstitutionRoomEntity
): Question {
    return Question(
        id = question.id,
        title = question.title,
        description = question.description,
        subjects = question.subjects,
        participant = br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToModel(
            participant
        ),
        institution = br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToModel(
            institution
        )
    )
}

fun convertToModel(question: QuestionApiModel): Question {
    return Question(
        id = question.id,
        title = question.title,
        description = question.description,
        subjects = question.subjects,
        participant = br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToModel(
            question.participant
        ),
        institution = br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToModel(
            question.institution
        )
    )
}

fun convertToRoomEntity(question: QuestionApiModel): QuestionRoomEntity {
    return QuestionRoomEntity(
        id = question.id,
        title = question.title,
        description = question.description,
        subjects = question.subjects,
        participantId = question.participant.id,
        institutionId = question.institution.id
    )
}