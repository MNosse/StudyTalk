package br.com.udesc.eso.tcc.studytalk.useCase.question

import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.gateway.QuestionGateway
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import org.springframework.stereotype.Service

@Service
class GetQuestionByIdUseCase(
    private val participantGateway: ParticipantGateway,
    private val questionGateway: QuestionGateway
) {
    @Throws(
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        QuestionNotFoundException::class
    )
    fun execute(input: Input): Output {
        questionGateway.getById(input.id)?.let { question ->
            participantGateway.getByUid(input.participantUid)?.let {
                if (it.institution!!.id == question.institution!!.id) {
                    return Output(question)
                } else throw ParticipantWithoutPermissionException()
            } ?: throw ParticipantNotFoundException()
        } ?: throw QuestionNotFoundException()
    }

    data class Input(
        val participantUid: String,
        val id: Long
    )

    data class Output(val question: Question)
}