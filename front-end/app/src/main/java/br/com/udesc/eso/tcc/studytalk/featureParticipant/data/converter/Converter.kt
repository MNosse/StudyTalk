package br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter

import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.entity.AnswerRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureInstitution.data.entity.InstitutionRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.entity.ParticipantRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.model.ParticipantApiModel
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.entity.QuestionRoomEntity

fun convertToModel(
    participant: ParticipantRoomEntity
): Participant {
    return Participant(
        id = participant.id,
        uid = participant.uid,
        name = participant.name,
        privilege = participant.privilege
    )
}

fun convertToModel(
    participant: ParticipantRoomEntity,
    institution: InstitutionRoomEntity?,
    favoriteQuestions: MutableList<QuestionRoomEntity>,
    likedAnswers: MutableList<AnswerRoomEntity>
): Participant {
    return Participant(
        id = participant.id,
        uid = participant.uid,
        name = participant.name,
        privilege = participant.privilege,
        institution = institution?.let {
            br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToModel(
                institution
            )
        },
        favoriteQuestions = favoriteQuestions.map { question ->
            br.com.udesc.eso.tcc.studytalk.featureQuestion.data.converter.convertToModel(question)
        }.toMutableList(),
        likedAnswers = likedAnswers.map { answer ->
            br.com.udesc.eso.tcc.studytalk.featureAnswer.data.converter.convertToModel(answer)
        }.toMutableList()
    )
}

fun convertToModel(participant: ParticipantApiModel): Participant {
    return Participant(
        id = participant.id,
        uid = participant.uid,
        name = participant.name,
        privilege = participant.privilege,
        institution = participant.institution?.let {
            br.com.udesc.eso.tcc.studytalk.featureInstitution.data.converter.convertToModel(it)
        },
        favoriteQuestions = if (participant.favoriteQuestions != null) {
            participant.favoriteQuestions.map { question ->
                br.com.udesc.eso.tcc.studytalk.featureQuestion.data.converter.convertToModel(
                    question
                )
            }.toMutableList()
        } else mutableListOf(),
        likedAnswers = if (participant.likedAnswers != null) {
            participant.likedAnswers.map { answer ->
                br.com.udesc.eso.tcc.studytalk.featureAnswer.data.converter.convertToModel(
                    answer
                )
            }.toMutableList()
        } else mutableListOf()
    )
}

fun convertToRoomEntity(participant: ParticipantApiModel): ParticipantRoomEntity {
    return ParticipantRoomEntity(
        id = participant.id,
        uid = participant.uid,
        name = participant.name,
        privilege = participant.privilege,
        institutionId = participant.institution?.id,
    )
}