package br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.model.Institution
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.repository.InstitutionRepository

class GetAllUseCase(
    private val repository: InstitutionRepository
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

    data class Output(val result: Result<MutableList<Institution>>)
}