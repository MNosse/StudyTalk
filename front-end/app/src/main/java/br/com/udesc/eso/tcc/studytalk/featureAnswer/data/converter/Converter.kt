package br.com.udesc.eso.tcc.studytalk.featureAnswer.data.converter

import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.entity.AnswerRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureAnswer.data.model.AnswerApiModel
import br.com.udesc.eso.tcc.studytalk.featureAnswer.domain.model.Answer
import br.com.udesc.eso.tcc.studytalk.featureParticipant.data.entity.ParticipantRoomEntity
import br.com.udesc.eso.tcc.studytalk.featureQuestion.data.entity.QuestionRoomEntity

fun convertToModel(
    answer: AnswerRoomEntity
): Answer {
    return Answer(
        id = answer.id,
        description = answer.description,
        likeCount = answer.likeCount
    )
}

fun convertToModel(
    answer: AnswerRoomEntity,
    question: QuestionRoomEntity,
    participant: ParticipantRoomEntity,
): Answer {
    return Answer(
        id = answer.id,
        description = answer.description,
        likeCount = answer.likeCount,
        question = br.com.udesc.eso.tcc.studytalk.featureQuestion.data.converter.convertToModel(
            question
        ),
        participant = br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToModel(
            participant
        )
    )
}

fun convertToModel(answer: AnswerApiModel): Answer {
    return Answer(
        id = answer.id,
        description = answer.description,
        likeCount = answer.likeCount,
        question = br.com.udesc.eso.tcc.studytalk.featureQuestion.data.converter.convertToModel(
            answer.question
        ),
        participant = br.com.udesc.eso.tcc.studytalk.featureParticipant.data.converter.convertToModel(
            answer.participant
        )
    )
}

fun convertToRoomEntity(answer: AnswerApiModel): AnswerRoomEntity {
    return AnswerRoomEntity(
        id = answer.id,
        description = answer.description,
        likeCount = answer.likeCount,
        questionId = answer.question.id,
        participantId = answer.participant.id
    )
}