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
class GetParticipantByUidUseCase(
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
                participantGateway.getByUid(input.participantToBeRetrievedUid)?.let {
                    return Output(it)
                } ?: throw ParticipantNotFoundException()
            } ?: throw AdministratorNotFoundException()
        } else participantGateway.getByUid(input.requestingUid)?.let {
            if (it.privilege == Privilege.PRINCIPAL || input.requestingUid == input.participantToBeRetrievedUid) {
                participantGateway.getByUid(input.participantToBeRetrievedUid)?.let {
                    return Output(it)
                } ?: throw ParticipantNotFoundException()
            } else throw ParticipantWithoutPrivilegeException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val requestingUid: String,
        val isAdministrator: Boolean,
        val participantToBeRetrievedUid: String
    )

    data class Output(val participant: Participant)
}