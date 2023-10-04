package br.com.udesc.eso.tcc.studytalk.useCase.administrador

import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import org.springframework.stereotype.Service

@Service
class CreateAdministratorUseCase(private val administratorGateway: AdministratorGateway) {
    fun execute(input: Input) {
        administratorGateway.create(uid = input.uid)
    }

    data class Input(val uid: String)
}