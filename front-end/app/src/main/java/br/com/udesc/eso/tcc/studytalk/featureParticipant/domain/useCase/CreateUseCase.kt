package br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureParticipant.domain.repository.ParticipantRepository

class CreateUseCase(
    private val repository: ParticipantRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.create(
                registrationCode = input.registrationCode,
                uid = input.uid,
                name = input.name
            )
        )
    }

    data class Input(
        val registrationCode: String,
        val uid: String,
        val name: String
    )

    data class Output(val result: Result<Unit>)
}