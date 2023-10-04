package br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.useCase.institution.GetAllInstitutionsUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/institutions")
class GetAllInstitutionsController(private val getAllInstitutionsUseCase: GetAllInstitutionsUseCase) : BaseController() {
    @GetMapping("/")
    @ResponseStatus(HttpStatus.FOUND)
    @Throws(AdministratorNotFoundException::class)
    fun getAllInstitutions(@RequestParam(name = "administratorUid") administratorUid: String): Response {
        return Response(
            convert(
                getAllInstitutionsUseCase.execute(
                    GetAllInstitutionsUseCase.Input(administratorUid = administratorUid)
                ).institutions
            )
        )
    }

    data class Response(
        val institutions: MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller.response.Response>
    )
}