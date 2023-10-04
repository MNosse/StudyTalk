package br.com.udesc.eso.tcc.studytalk.useCase.participant

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import org.springframework.stereotype.Service

@Service
class GetAllParticipantsByInstitutionUseCase(
    private val administratorGateway: AdministratorGateway,
    private val participantGateway: ParticipantGateway
) {
    @Throws(
        AdministratorNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun execute(input: Input): Output {
        if (input.isAdministrator) {
            administratorGateway.getByUid(input.requestingUid)?.let {
                return Output(participantGateway.getAllByInstitutionId(input.institutionId))
            } ?: throw AdministratorNotFoundException()
        } else participantGateway.getByUid(input.requestingUid)?.let {
            if (it.privilege == Privilege.PRINCIPAL) {
                return Output(participantGateway.getAllByInstitutionId(input.institutionId))
            } else throw ParticipantWithoutPrivilegeException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val requestingUid: String,
        val isAdministrator: Boolean,
        val institutionId: Long
    )

    data class Output(val participants: MutableList<Participant>)
}