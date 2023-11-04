package br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.response.Response
import br.com.udesc.eso.tcc.studytalk.useCase.institution.GetInstitutionByRegistrationCodeUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/institutions/registration_code")
class GetInstitutionByRegistrationCodeController(private val getInstitutionByRegistrationCodeUseCase: GetInstitutionByRegistrationCodeUseCase) :
    BaseController() {
    @GetMapping("/{registrationCode}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        AdministratorNotFoundException::class,
        InstitutionNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class
    )
    fun getInstitutionByRegistrationCode(
        @PathVariable registrationCode: String,
        @RequestParam requestingUid: String,
        @RequestParam isAdministrator: Boolean,
    ): Response {
        return convert(
            getInstitutionByRegistrationCodeUseCase.execute(
                GetInstitutionByRegistrationCodeUseCase.Input(
                    requestingUid = requestingUid,
                    isAdministrator = isAdministrator,
                    registrationCode = registrationCode
                )
            ).institution
        )
    }
}