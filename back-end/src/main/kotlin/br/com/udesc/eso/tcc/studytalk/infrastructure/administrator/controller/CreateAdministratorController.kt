package br.com.udesc.eso.tcc.studytalk.infrastructure.administrator.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.useCase.administrador.CreateAdministratorUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/administrators")
@Validated
class CreateAdministratorController(private val createAdministratorUseCase: CreateAdministratorUseCase) : BaseController() {
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createAdministrator(@Valid @RequestBody request: Request) {
        createAdministratorUseCase.execute(CreateAdministratorUseCase.Input(request.uid))
    }

    data class Request(
        @field:NotBlank(message = "O UID deve conter ao menos um caractere.")
        val uid: String
    )
}