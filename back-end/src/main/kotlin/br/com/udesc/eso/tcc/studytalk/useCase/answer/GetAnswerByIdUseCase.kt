package br.com.udesc.eso.tcc.studytalk.useCase.answer

import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.answer.gateway.AnswerGateway
import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import org.springframework.stereotype.Service

@Service
class GetAnswerByIdUseCase(
    private val answerGateway: AnswerGateway,
    private val participantGateway: ParticipantGateway
) {
    @Throws(
        AnswerNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class
    )
    fun execute(input: Input): Output {
        answerGateway.getById(input.id)?.let { answer ->
            participantGateway.getByUid(input.participantUid)?.let { participant ->
                if (participant.institution!!.id == answer.question!!.institution!!.id) {
                    return Output(answer)
                } else throw ParticipantWithoutPermissionException()
            } ?: throw ParticipantNotFoundException()
        } ?: throw AnswerNotFoundException()
    }

    data class Input(
        val participantUid: String,
        val id: Long
    )

    data class Output(val answer: Answer)
}