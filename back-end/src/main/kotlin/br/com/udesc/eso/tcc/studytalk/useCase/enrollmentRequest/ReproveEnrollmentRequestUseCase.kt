package br.com.udesc.eso.tcc.studytalk.useCase.enrollmentRequest

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.exception.EnrollmentRequestNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.gateway.EnrollmentRequestGateway
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.report.exception.ReportNotFoundException
import org.springframework.stereotype.Service

@Service
class ReproveEnrollmentRequestUseCase(
    private val enrollmentRequestGateway: EnrollmentRequestGateway,
    private val participantGateway: ParticipantGateway
) {
    @Throws(
        EnrollmentRequestNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun execute(input: Input) {
        participantGateway.getByUid(input.reproverUid)?.let { participant ->
            if (participant.privilege == Privilege.TEACHER || participant.privilege == Privilege.PRINCIPAL) {
                enrollmentRequestGateway.getById(input.id)?.let {
                    if (participant.institution!!.id == it.institution.id) {
                        enrollmentRequestGateway.reprove(input.id)
                    } else throw ParticipantWithoutPermissionException()
                } ?: throw EnrollmentRequestNotFoundException()
            } else throw ParticipantWithoutPrivilegeException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val reproverUid: String,
        val id: Long
    )
}