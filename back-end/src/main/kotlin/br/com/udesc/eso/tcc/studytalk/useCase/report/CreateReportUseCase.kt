package br.com.udesc.eso.tcc.studytalk.useCase.report

import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.answer.gateway.AnswerGateway
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.gateway.QuestionGateway
import br.com.udesc.eso.tcc.studytalk.entity.report.gateway.ReportGateway
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class CreateReportUseCase(
    private val answerGateway: AnswerGateway,
    private val participantGateway: ParticipantGateway,
    private val questionGateway: QuestionGateway,
    private val reportGateway: ReportGateway
) {
    @Throws(
        AnswerNotFoundException::class,
        QuestionNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class
    )
    fun execute(input: Input) {
        participantGateway.getByUid(input.requestingParticipantUid)?.let {
            var postableInstitutionId = 0L
            when (input.postableType) {
                "ANSWER" -> {
                    answerGateway.getById(input.postableId)?.let {
                        postableInstitutionId = it.question!!.institution!!.id
                    } ?: throw AnswerNotFoundException()
                }

                "QUESTION" -> {
                    questionGateway.getById(input.postableId)?.let {
                        postableInstitutionId = it.institution!!.id
                    } ?: throw QuestionNotFoundException()
                }
            }

            if (it.institution!!.id == postableInstitutionId) {
                reportGateway.create(
                    postableId = input.postableId,
                    postableType = input.postableType,
                    description = input.description
                )
            } else throw ParticipantWithoutPermissionException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val requestingParticipantUid: String,
        val postableId: Long,
        val postableType: String,
        val description: String
    )
}