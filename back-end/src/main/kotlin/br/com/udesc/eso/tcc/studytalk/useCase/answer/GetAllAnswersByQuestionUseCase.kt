package br.com.udesc.eso.tcc.studytalk.useCase.answer

import br.com.udesc.eso.tcc.studytalk.entity.answer.gateway.AnswerGateway
import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.gateway.QuestionGateway
import org.springframework.stereotype.Service

@Service
class GetAllAnswersByQuestionUseCase(
    private val answerGateway: AnswerGateway,
    private val participantGateway: ParticipantGateway,
    private val questionGateway: QuestionGateway
) {
    @Throws(
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        QuestionNotFoundException::class
    )
    fun execute(input: Input): Output {
        participantGateway.getByUid(input.participantUid)?.let { participant ->
            questionGateway.getById(input.questionId)?.let { question ->
                if (participant.institution!!.id == question.institution!!.id) {
                    return Output(answerGateway.getAllByQuestionId(input.questionId))
                } else throw ParticipantWithoutPermissionException()
            } ?: throw QuestionNotFoundException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val participantUid: String,
        val questionId: Long
    )

    data class Output(val answers: MutableList<Answer>)
}