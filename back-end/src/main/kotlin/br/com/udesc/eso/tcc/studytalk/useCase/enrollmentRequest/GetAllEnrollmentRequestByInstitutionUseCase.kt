package br.com.udesc.eso.tcc.studytalk.useCase.enrollmentRequest

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.gateway.EnrollmentRequestGateway
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.model.EnrollmentRequest
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import org.springframework.stereotype.Service

@Service
class GetAllEnrollmentRequestByInstitutionUseCase(
    private val enrollmentRequestGateway: EnrollmentRequestGateway,
    private val participantGateway: ParticipantGateway
) {
    @Throws(
        ParticipantNotFoundException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun execute(input: Input): Output {
        participantGateway.getByUid(input.requestingParticipantUid)?.let {
            if (it.privilege == Privilege.TEACHER || it.privilege == Privilege.PRINCIPAL) {
                return Output(enrollmentRequestGateway.getAllByInstitutionId(input.institutionId))
            } else throw ParticipantWithoutPrivilegeException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val requestingParticipantUid: String,
        val institutionId: Long
    )

    data class Output(val enrollmentRequesties: MutableList<EnrollmentRequest>)
}