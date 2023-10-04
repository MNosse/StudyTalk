package br.com.udesc.eso.tcc.studytalk.useCase.institution

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import br.com.udesc.eso.tcc.studytalk.entity.institution.gateway.InstitutionGateway
import org.springframework.stereotype.Service

@Service
class CreateInstitutionUseCase(
    private val administratorGateway: AdministratorGateway,
    private val institutionGateway: InstitutionGateway
) {
    @Throws(AdministratorNotFoundException::class)
    fun execute(input: Input) {
        administratorGateway.getByUid(input.administratorUid)?.let {
            institutionGateway.create(name = input.name)
        } ?: throw AdministratorNotFoundException()
    }

    data class Input(
        val administratorUid: String,
        val name: String
    )
}