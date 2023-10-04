package br.com.udesc.eso.tcc.studytalk.useCase.institution

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import br.com.udesc.eso.tcc.studytalk.entity.institution.gateway.InstitutionGateway
import br.com.udesc.eso.tcc.studytalk.entity.institution.model.Institution
import org.springframework.stereotype.Service

@Service
class GetAllInstitutionsUseCase(
    private val administratorGateway: AdministratorGateway,
    private val institutionGateway: InstitutionGateway
) {
    @Throws(AdministratorNotFoundException::class)
    fun execute(input: Input): Output {
        administratorGateway.getByUid(input.administratorUid)?.let {
            return Output(institutionGateway.getAll())
        } ?: throw AdministratorNotFoundException()
    }

    data class Input(val administratorUid: String)
    data class Output(val institutions: MutableList<Institution>)
}