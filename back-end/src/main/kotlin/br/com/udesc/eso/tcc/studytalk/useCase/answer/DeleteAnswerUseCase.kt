package br.com.udesc.eso.tcc.studytalk.useCase.answer

import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.answer.gateway.AnswerGateway
import org.springframework.stereotype.Service

@Service
class DeleteAnswerUseCase(private val answerGateway: AnswerGateway) {
    @Throws(
        AnswerIsNotFromParticipantException::class,
        AnswerNotFoundException::class
    )
    fun execute(input: Input) {
        answerGateway.getById(input.id)?.let {
            if (it.participant!!.uid == input.participantUid) {
                answerGateway.delete(input.id)
            } else throw AnswerIsNotFromParticipantException()
        } ?: throw AnswerNotFoundException()
    }

    data class Input(val id: Long, val participantUid: String)
}