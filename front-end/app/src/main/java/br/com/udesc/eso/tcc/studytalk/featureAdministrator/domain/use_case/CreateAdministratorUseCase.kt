package br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.use_case

import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.repository.AdministratorRepository

class CreateAdministratorUseCase(
    private val repository: AdministratorRepository
) {
    suspend operator fun invoke(input: Input) {
        repository.create(uid = input.uid)
    }

    data class Input(val uid: String)
}