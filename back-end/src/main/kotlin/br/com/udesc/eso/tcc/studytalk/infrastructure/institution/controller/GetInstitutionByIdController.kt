package br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.response.Response
import br.com.udesc.eso.tcc.studytalk.useCase.institution.GetInstitutionByIdUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/institutions")
class GetInstitutionByIdController(private val getInstitutionByIdUseCase: GetInstitutionByIdUseCase) :
    BaseController() {
    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        AdministratorNotFoundException::class,
        InstitutionNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class
    )
    fun getInstitutionById(
        @PathVariable id: Long,
        @RequestParam requestingUid: String,
        @RequestParam isAdministrator: Boolean,
    ): Response {
        return convert(
            getInstitutionByIdUseCase.execute(
                GetInstitutionByIdUseCase.Input(
                    requestingUid = requestingUid,
                    isAdministrator = isAdministrator,
                    id = id
                )
            ).institution
        )
    }
}