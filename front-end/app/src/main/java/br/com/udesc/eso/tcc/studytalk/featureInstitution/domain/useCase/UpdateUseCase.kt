package br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.repository.InstitutionRepository

class UpdateUseCase(
    private val repository: InstitutionRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.update(
                id = input.id,
                administratorUid = input.administratorUid,
                name = input.name
            )
        )
    }

    data class Input(
        val id: Long,
        val administratorUid: String,
        val name: String
    )

    data class Output(val result: Result<Unit>)
}