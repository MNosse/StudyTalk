package br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.useCase

import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.model.Administrator
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.repository.AdministratorRepository

class GetByUidUseCase(
    private val repository: AdministratorRepository
) {
    @Throws(Exception::class)
    suspend operator fun invoke(input: Input): Output {
        return Output(repository.getAdministratorByUid(uid = input.uid))
    }

    data class Input(val uid: String)
    data class Output(val result: Result<Administrator?>)
}