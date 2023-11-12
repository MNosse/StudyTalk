package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.repository.ParticipantRepository

class GetAllByInstitutionUseCase(
    private val repository: ParticipantRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.getAllByInstitution(
                requestingUid = input.requestingUid,
                isAdministrator = input.isAdministrator,
                institutionId = input.institutionId
            )
        )
    }

    data class Input(
        val requestingUid: String,
        val isAdministrator: Boolean,
        val institutionId: Long
    )

    data class Output(val result: Result<MutableList<Participant>>)
}