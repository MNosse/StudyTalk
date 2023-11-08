package br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.repository.InstitutionRepository

class DeleteUseCase(
    private val repository: InstitutionRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.delete(
                id = input.id,
                administratorUid = input.administratorUid
            )
        )
    }

    data class Input(
        val id: Long,
        val administratorUid: String
    )

    data class Output(val result: Result<Unit>)
}