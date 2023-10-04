package br.com.udesc.eso.tcc.studytalk.useCase.participant

import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import org.springframework.stereotype.Service

@Service
class DeleteParticipantUseCase(private val participantGateway: ParticipantGateway) {
    @Throws(ParticipantNotFoundException::class, ParticipantWithoutPermissionException::class)
    fun execute(input: Input) {
        if (input.requestingParticipantUid == input.participantToBeDeletedUid) {
            participantGateway.getByUid(input.participantToBeDeletedUid)?.let {
                participantGateway.delete(input.participantToBeDeletedUid)
            } ?: throw ParticipantNotFoundException()
        } else throw ParticipantWithoutPermissionException()
    }

    data class Input(
        val requestingParticipantUid: String,
        val participantToBeDeletedUid: String
    )
}