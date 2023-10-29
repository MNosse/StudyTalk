package br.com.udesc.eso.tcc.studytalk.useCase.participant

import br.com.udesc.eso.tcc.studytalk.entity.answer.model.Answer
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.gateway.QuestionGateway
import org.springframework.stereotype.Service

@Service
class AnswerAQuestionUseCase(
    private val participantGateway: ParticipantGateway,
    private val questionGateway: QuestionGateway
) {
    @Throws(ParticipantNotFoundException::class, QuestionNotFoundException::class)
    fun execute(input: Input): Output {
        participantGateway.getByUid(input.participantUid)?.let {
            questionGateway.getById(input.questionId)?.let {
                participantGateway.answerAQuestion(
                    participantUid = input.participantUid,
                    questionId = input.questionId,
                    description = input.description
                )?.let {
                    return Output(it)
                }
            } ?: throw QuestionNotFoundException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val participantUid: String,
        val questionId: Long,
        val description: String
    )

    data class Output(val answer: Answer)
}