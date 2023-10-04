package br.com.udesc.eso.tcc.studytalk.useCase.report

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.report.exception.ReportNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.report.gateway.ReportGateway
import br.com.udesc.eso.tcc.studytalk.entity.report.model.Report
import org.springframework.stereotype.Service

@Service
class GetReportByIdUseCase(
    private val participantGateway: ParticipantGateway,
    private val reportGateway: ReportGateway
) {
    @Throws(
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        ParticipantWithoutPrivilegeException::class,
        ReportNotFoundException::class
    )
    fun execute(input: Input): Output {
        participantGateway.getByUid(input.requestingParticipantUid)?.let { participant ->
            if (participant.privilege == Privilege.TEACHER || participant.privilege == Privilege.PRINCIPAL) {
                reportGateway.getById(input.id)?.let {
                    if (participant.institution!!.id == it.institution!!.id) {
                        return Output(it)
                    } else throw ParticipantWithoutPermissionException()
                } ?: throw ReportNotFoundException()
            } else throw ParticipantWithoutPrivilegeException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val requestingParticipantUid: String,
        val id: Long
    )

    data class Output(val report: Report)
}