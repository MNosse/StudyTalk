package br.com.udesc.eso.tcc.studytalk.useCase.participant

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import br.com.udesc.eso.tcc.studytalk.entity.participant.model.Participant
import org.springframework.stereotype.Service

@Service
class GetAllParticipantsUseCase(
    private val administratorGateway: AdministratorGateway,
    private val participantGateway: ParticipantGateway
) {
    @Throws(AdministratorNotFoundException::class)
    fun execute(input: Input): Output {
        administratorGateway.getByUid(input.administratorUid)?.let {
            return Output(participantGateway.getAll())
        } ?: throw AdministratorNotFoundException()
    }

    data class Input(val administratorUid: String)
    data class Output(val participants: MutableList<Participant>)
}