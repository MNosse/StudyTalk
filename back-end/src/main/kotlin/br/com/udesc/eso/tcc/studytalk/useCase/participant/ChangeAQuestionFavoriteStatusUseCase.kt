package br.com.udesc.eso.tcc.studytalk.useCase.participant

import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.gateway.QuestionGateway
import org.springframework.stereotype.Service

@Service
class ChangeAQuestionFavoriteStatusUseCase(
    private val participantGateway: ParticipantGateway,
    private val questionGateway: QuestionGateway
) {
    @Throws(ParticipantNotFoundException::class, QuestionNotFoundException::class)
    fun execute(input: Input) {
        participantGateway.getByUid(input.participantUid)?.let {
            questionGateway.getById(input.questionId)?.let {
                participantGateway.changeAQuestionFavoriteStatus(
                    participantUid = input.participantUid,
                    questionId = input.questionId
                )
            } ?: throw QuestionNotFoundException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val participantUid: String,
        val questionId: Long
    )
}