package br.com.udesc.eso.tcc.studytalk.useCase.report

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.report.gateway.ReportGateway
import br.com.udesc.eso.tcc.studytalk.entity.report.model.Report
import org.springframework.stereotype.Service

@Service
class GetAllReportsByInstitutionUseCase(
    private val participantGateway: ParticipantGateway,
    private val reportGateway: ReportGateway
) {
    @Throws(
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun execute(input: Input): Output {
        participantGateway.getByUid(input.requestingParticipantUid)?.let {
            if (it.institution!!.id == input.id) {
                if (it.privilege == Privilege.TEACHER || it.privilege == Privilege.PRINCIPAL) {
                    return Output(reportGateway.getAllByInstitutionId(input.id))
                } else throw ParticipantWithoutPrivilegeException()
            } else throw ParticipantWithoutPermissionException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val requestingParticipantUid: String,
        val id: Long
    )

    data class Output(val reports: MutableList<Report>)
}