package br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.controller.response.Response
import br.com.udesc.eso.tcc.studytalk.useCase.administrador.GetAdministratorByUidUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/administrators")
class GetAdministratorByUidController(private val getAdministratorByUidUseCase: GetAdministratorByUidUseCase) :
    BaseController() {
    @GetMapping("/{uid}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(AdministratorNotFoundException::class)
    fun getAdministratorByUid(@PathVariable uid: String): Response {
        return convert(getAdministratorByUidUseCase.execute(GetAdministratorByUidUseCase.Input(uid)).administrator)
    }
}