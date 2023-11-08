package br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.model.Institution
import br.com.udesc.eso.tcc.studytalk.featureInstitution.domain.repository.InstitutionRepository

class GetByIdUseCase(
    private val repository: InstitutionRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(
            repository.getById(
                id = input.id,
                requestingUid = input.requestingUid,
                isAdministrator = input.isAdministrator
            )
        )
    }

    data class Input(
        val id: Long,
        val requestingUid: String,
        val isAdministrator: Boolean
    )

    data class Output(val result: Result<Institution?>)
}