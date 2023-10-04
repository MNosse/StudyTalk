package br.com.udesc.eso.tcc.studytalk.useCase.administrador

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import br.com.udesc.eso.tcc.studytalk.entity.administrator.model.Administrator
import org.springframework.stereotype.Service

@Service
class GetAdministratorByUidUseCase(private val administratorGateway: AdministratorGateway) {
    @Throws(AdministratorNotFoundException::class)
    fun execute(input: Input): Output {
        administratorGateway.getByUid(input.uid)?.let {
            return Output(it)
        } ?: throw AdministratorNotFoundException()
    }

    data class Input(val uid: String)
    data class Output(val administrator: Administrator)
}