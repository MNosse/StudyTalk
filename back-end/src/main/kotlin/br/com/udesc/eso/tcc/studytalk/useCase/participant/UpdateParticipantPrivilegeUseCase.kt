package br.com.udesc.eso.tcc.studytalk.useCase.participant

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import org.springframework.stereotype.Service

@Service
class UpdateParticipantPrivilegeUseCase(
    private val administratorGateway: AdministratorGateway,
    private val participantGateway: ParticipantGateway
) {
    @Throws(
        AdministratorNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun execute(input: Input) {
        if (input.isAdministrator) {
            administratorGateway.getByUid(input.requestingUid)?.let {
                participantGateway.getByUid(input.participantToBeUpdatedUid)?.let {
                    participantGateway.updatePrivilege(
                        uid = input.participantToBeUpdatedUid,
                        privilege = input.privilege
                    )
                } ?: throw ParticipantNotFoundException()
            } ?: throw AdministratorNotFoundException()
        } else participantGateway.getByUid(input.requestingUid)?.let {
            if (it.privilege == Privilege.PRINCIPAL && input.privilege == Privilege.TEACHER) {
                participantGateway.getByUid(input.participantToBeUpdatedUid)?.let {
                    participantGateway.updatePrivilege(
                        uid = input.participantToBeUpdatedUid,
                        privilege = input.privilege
                    )
                } ?: throw ParticipantNotFoundException()
            } else throw ParticipantWithoutPrivilegeException()
        } ?: throw ParticipantNotFoundException()
    }

    data class Input(
        val requestingUid: String,
        val isAdministrator: Boolean,
        val participantToBeUpdatedUid: String,
        val privilege: Privilege
    )
}