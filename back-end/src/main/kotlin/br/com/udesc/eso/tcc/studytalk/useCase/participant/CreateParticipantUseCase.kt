package br.com.udesc.eso.tcc.studytalk.useCase.participant

import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.gateway.InstitutionGateway
import br.com.udesc.eso.tcc.studytalk.entity.participant.gateway.ParticipantGateway
import org.springframework.stereotype.Service

@Service
class CreateParticipantUseCase(
    private val institutionGateway: InstitutionGateway,
    private val participantGateway: ParticipantGateway
) {
    @Throws(InstitutionNotFoundException::class)
    fun execute(input: Input) {
        institutionGateway.getByRegistrationCode(input.registrationCode)?.let {
            participantGateway.create(
                registrationCode = input.registrationCode,
                uid = input.uid,
                name = input.name
            )
        } ?: throw InstitutionNotFoundException()
    }

    data class Input(
        val registrationCode: String,
        val uid: String,
        val name: String
    )
}