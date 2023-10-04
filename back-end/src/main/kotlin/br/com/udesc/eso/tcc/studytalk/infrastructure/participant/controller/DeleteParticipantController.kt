package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.useCase.participant.DeleteParticipantUseCase
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/participants")
@Validated
class DeleteParticipantController(private val deleteParticipantUseCase: DeleteParticipantUseCase) : BaseController() {
    @DeleteMapping("/{participantToBeDeletedUid}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(ParticipantNotFoundException::class, ParticipantWithoutPermissionException::class)
    fun deleteParticipantByUid(
        @PathVariable participantToBeDeletedUid: String,
        @RequestParam(name = "requestingParticipantUid") requestingParticipantUid: String
    ) {
        deleteParticipantUseCase.execute(
            DeleteParticipantUseCase.Input(
                requestingParticipantUid = requestingParticipantUid,
                participantToBeDeletedUid = participantToBeDeletedUid
            )
        )
    }
}