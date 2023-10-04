package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller

import br.com.udesc.eso.tcc.studytalk.core.enums.Privilege
import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.useCase.participant.UpdateParticipantPrivilegeUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/participants")
@Validated
class UpdateParticipantPrivilegeController(private val updateParticipantPrivilegeUseCase: UpdateParticipantPrivilegeUseCase) : BaseController() {
    @PutMapping("/{participantToBeUpdatedUid}/privilege/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        AdministratorNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun updateParticipantPrivilege(
        @PathVariable participantToBeUpdatedUid: String,
        @Valid @RequestBody request: Request
    ) {
        updateParticipantPrivilegeUseCase.execute(
            UpdateParticipantPrivilegeUseCase.Input(
                requestingUid = request.requestingUid,
                isAdministrator = request.isAdministrator,
                participantToBeUpdatedUid = participantToBeUpdatedUid,
                privilege = request.privilege
            )
        )
    }

    data class Request(
        val requestingUid: String,
        val isAdministrator: Boolean,
        val privilege: Privilege
    )
}