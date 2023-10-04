package br.com.udesc.eso.tcc.studytalk.useCase.report

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.report.exception.ReportNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.report.gateway.ReportGateway
import org.springframework.stereotype.Service

@Service
class ReproveReportUseCase(
    private val participantGateway: ParticipantGateway,
    private val reportGateway: ReportGateway
) {
    @Throws(
        ReportNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun execute(input: Input) {
        participantGateway.getByUid(input.reproverUid)?.let { participant ->
            if (participant.privilege == Privilege.TEACHER || participant.privilege == Privilege.PRINCIPAL) {
                reportGateway.getById(input.id)?.let {
                    if (it.institution!!.id == participant.institution!!.id) {
                        reportGateway.reprove(input.id)
                    } else throw ParticipantWithoutPermissionException()
                } ?: throw ReportNotFoundException()
            } else throw ParticipantWithoutPrivilegeException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val reproverUid: String,
        val id: Long
    )
}