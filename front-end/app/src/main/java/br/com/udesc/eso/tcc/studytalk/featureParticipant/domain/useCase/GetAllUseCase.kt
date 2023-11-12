package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.model.Participant
import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.repository.ParticipantRepository

class GetAllUseCase(
    private val repository: ParticipantRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.getAll(
                administratorUid = input.administratorUid,
            )
        )
    }

    data class Input(
        val administratorUid: String
    )

    data class Output(val result: Result<MutableList<Participant>>)
}