package br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.use_case

import br.com.udesc.eso.tcc.studytalk.featureAdministrator.data.repository.AdministratorRepository
import br.com.udesc.eso.tcc.studytalk.featureAdministrator.domain.model.Administrator

class GetAdministratorByUidUseCase(
    private val repository: AdministratorRepository
) {
//    suspend operator fun invoke(input: Input): Output {
//        repository.getAdministratorByUid(uid = input.uid)?.let {
//            return Output(administrator = it)
//        }
//    }

    data class Input(val uid: String)
    data class Output(val administrator: Administrator)
}