package br.com.udesc.eso.tcc.studytalk.infrastructure.institution.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.institution.UpdateInstitutionUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/institutions")
@Validated
class UpdateInstitutionController(private val updateInstitutionUseCase: UpdateInstitutionUseCase) : BaseController() {
    @PutMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        AdministratorNotFoundException::class,
        InstitutionNotFoundException::class
    )
    fun updateInstitution(@PathVariable id: Long, @Valid @RequestBody request: Request) {
        updateInstitutionUseCase.execute(
            UpdateInstitutionUseCase.Input(
                administratorUid = request.administratorUid,
                id = id,
                name = request.name
            )
        )
    }

    data class Request(
        val administratorUid: String,
        @field:NotBlank(message = "O nome deve conter ao menos um caractere.")
        @field:Size(max = 256, message = "O nome não pode ultrapassar 256 caracteres.")
        val name: String
    )
}