package br.com.udesc.eso.tcc.studytalk.useCase.institution

import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.administrator.gateway.AdministratorGateway
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.gateway.InstitutionGateway
import org.springframework.stereotype.Service

@Service
class UpdateInstitutionUseCase(
    private val administratorGateway: AdministratorGateway,
    private val institutionGateway: InstitutionGateway
) {
    @Throws(
        AdministratorNotFoundException::class,
        InstitutionNotFoundException::class
    )
    fun execute(input: Input) {
        administratorGateway.getByUid(input.administratorUid)?.let {
            institutionGateway.getById(input.id)?.let {
                institutionGateway.update(id = input.id, name = input.name)
            } ?: throw InstitutionNotFoundException()
        } ?: throw AdministratorNotFoundException()
    }

    data class Input(
        val administratorUid: String,
        val id: Long,
        val name: String
    )
}