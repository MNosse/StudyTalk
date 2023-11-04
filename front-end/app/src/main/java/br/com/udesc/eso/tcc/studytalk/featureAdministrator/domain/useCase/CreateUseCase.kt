package br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.repository.AdministratorRepository

class CreateUseCase(
    private val repository: AdministratorRepository
) {
    suspend operator fun invoke(input: Input): Output {
        return Output(repository.create(uid = input.uid))
    }

    data class Input(val uid: String)

    data class Output(val result: Result<Unit>)
}