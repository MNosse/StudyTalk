package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.useCase.participant.UpdateParticipantUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/participants")
@Validated
class UpdateParticipantController(private val updateParticipantUseCase: UpdateParticipantUseCase) : BaseController() {
    @PutMapping("/{participantToBeUpdatedUid}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(ParticipantNotFoundException::class, ParticipantWithoutPermissionException::class)
    fun updateParticipant(@PathVariable participantToBeUpdatedUid: String, @Valid @RequestBody request: Request) {
        updateParticipantUseCase.execute(
            UpdateParticipantUseCase.Input(
                requestingParticipantUid = request.requestingParticipantUid,
                participantToBeUpdatedUid = participantToBeUpdatedUid,
                name = request.name
            )
        )
    }

    data class Request(
        val requestingParticipantUid: String,
        @field:NotBlank(message = "O nome deve conter ao menos um caractere.")
        @field:Size(max = 128, message = "O nome não pode ultrapassar 128 caracteres."
        ) val name: String
    )
}