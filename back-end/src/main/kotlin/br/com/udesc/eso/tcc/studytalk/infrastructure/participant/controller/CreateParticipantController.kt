package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.institution.exception.InstitutionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.response.Response
import br.com.udesc.eso.tcc.studytalk.useCase.participant.CreateParticipantUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/participants")
@Validated
class CreateParticipantController(private val createParticipantUseCase: CreateParticipantUseCase) : BaseController() {
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @Throws(InstitutionNotFoundException::class)
    fun createParticipant(@Valid @RequestBody request: Request): Response {
        return convert(
            createParticipantUseCase.execute(
                CreateParticipantUseCase.Input(
                    request.registrationCode,
                    request.uid,
                    request.name
                )
            ).participant
        )
    }

    data class Request(
        val registrationCode: String,
        @field:NotBlank(message = "O UID deve conter ao menos um caractere.")
        val uid: String,
        @field:NotBlank(message = "O nome deve conter ao menos um caractere.")
        @field:Size(max = 128, message = "O nome não pode ultrapassar 128 caracteres.")
        val name: String
    )
}