package br.com.udesc.eso.tcc.studytalk.useCase.participant

import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import org.springframework.stereotype.Service

@Service
class UpdateParticipantUseCase(private val participantGateway: ParticipantGateway) {
    @Throws(ParticipantNotFoundException::class, ParticipantWithoutPermissionException::class)
    fun execute(input: Input) {
        if (input.requestingParticipantUid == input.participantToBeUpdatedUid) {
            participantGateway.getByUid(input.participantToBeUpdatedUid)?.let {
                participantGateway.update(uid = input.participantToBeUpdatedUid, name = input.name)
            } ?: throw ParticipantNotFoundException()
        } else throw ParticipantWithoutPermissionException()
    }

    data class Input(
        val requestingParticipantUid: String,
        val participantToBeUpdatedUid: String,
        val name: String
    )
}