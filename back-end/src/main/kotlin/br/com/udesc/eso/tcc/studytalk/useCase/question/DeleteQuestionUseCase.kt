package br.com.udesc.eso.tcc.studytalk.useCase.question

import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.gateway.QuestionGateway
import org.springframework.stereotype.Service

@Service
class DeleteQuestionUseCase(private val questionGateway: QuestionGateway) {
    @Throws(
        QuestionIsNotFromParticipantException::class,
        QuestionNotFoundException::class
    )
    fun execute(input: Input) {
        questionGateway.getById(input.id)?.let {
            if (it.participant!!.uid == input.participantUid) {
                questionGateway.delete(input.id)
            } else throw QuestionIsNotFromParticipantException()
        } ?: throw QuestionNotFoundException()
    }

    data class Input(val id: Long, val participantUid: String)
}