package br.com.udesc.eso.tcc.studytalk.useCase.administrador

import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import br.com.udesc.eso.tcc.studytalk.entity.administrator.model.Administrator
import org.springframework.stereotype.Service

@Service
class CreateAdministratorUseCase(private val administratorGateway: AdministratorGateway) {
    fun execute(input: Input): Output {
        return Output(administratorGateway.create(uid = input.uid))
    }

    data class Input(val uid: String)
    data class Output(val administrator: Administrator)
}