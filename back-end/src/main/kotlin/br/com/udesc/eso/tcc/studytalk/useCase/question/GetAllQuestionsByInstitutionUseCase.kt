package br.com.udesc.eso.tcc.studytalk.useCase.question

import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.question.gateway.QuestionGateway
import br.com.udesc.eso.tcc.studytalk.entity.question.model.Question
import org.springframework.stereotype.Service

@Service
class GetAllQuestionsByInstitutionUseCase(
    private val participantGateway: ParticipantGateway,
    private val questionGateway: QuestionGateway
) {
    @Throws(ParticipantNotFoundException::class, ParticipantWithoutPermissionException::class)
    fun execute(input: Input): Output {
        participantGateway.getByUid(input.participantUid)?.let {
            if (it.institution!!.id == input.institutionId) {
                return Output(questionGateway.getAllByInstitutionId(input.institutionId))
            } else throw ParticipantWithoutPermissionException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val participantUid: String,
        val institutionId: Long
    )

    data class Output(val questions: MutableList<Question>)
}