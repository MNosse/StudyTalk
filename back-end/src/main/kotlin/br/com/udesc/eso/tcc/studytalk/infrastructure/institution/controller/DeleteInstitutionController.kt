package br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.institution.DeleteInstitutionUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/institutions")
class DeleteInstitutionController(private val deleteInstitutionUseCase: DeleteInstitutionUseCase) : BaseController() {
    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        AdministratorNotFoundException::class,
        InstitutionNotFoundException::class
    )
    fun deleteInstitutionById(
        @PathVariable id: Long,
        @RequestParam(name = "administratorUid") administratorUid: String
    ) {
        deleteInstitutionUseCase.execute(
            DeleteInstitutionUseCase.Input(
                administratorUid = administratorUid,
                id = id
            )
        )
    }
}