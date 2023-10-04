package br.com.udesc.eso.tcc.studytalk.useCase.participant

import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.answer.gateway.AnswerGateway
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import org.springframework.stereotype.Service

@Service
class ChangeAnAnswerLikeStatusUseCase(
    private val answerGateway: AnswerGateway,
    private val participantGateway: ParticipantGateway
) {
    @Throws(ParticipantNotFoundException::class, AnswerNotFoundException::class)
    fun execute(input: Input) {
        participantGateway.getByUid(input.participantUid)?.let {
            answerGateway.getById(input.answerId)?.let {
                participantGateway.changeAnAnswerLikeStatus(
                    participantUid = input.participantUid,
                    answerId = input.answerId
                )
            } ?: throw AnswerNotFoundException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val participantUid: String,
        val answerId: Long
    )
}