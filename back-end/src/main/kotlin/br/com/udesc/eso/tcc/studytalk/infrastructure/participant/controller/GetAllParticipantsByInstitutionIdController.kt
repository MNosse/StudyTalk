package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.useCase.participant.GetAllParticipantsByInstitutionUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/participants/institution")
class GetAllParticipantsByInstitutionIdController(private val getAllParticipantsByInstitutionUseCase: GetAllParticipantsByInstitutionUseCase) :
    BaseController() {
    @GetMapping("/{institutionId}/")
    @ResponseStatus(HttpStatus.FOUND)
    @Throws(
        AdministratorNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun getAllParticipantsByInstitutionId(
        @PathVariable institutionId: Long,
        @RequestParam(name = "requestingUid") requestingUid: String,
        @RequestParam(name = "isAdministrator") isAdministrator: Boolean
    ): Response {
        return Response(
            convert(
                getAllParticipantsByInstitutionUseCase.execute(
                    GetAllParticipantsByInstitutionUseCase.Input(
                        requestingUid = requestingUid,
                        isAdministrator = isAdministrator,
                        institutionId = institutionId
                    )
                ).participants
            )
        )
    }

    data class Response(
        val participants: MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.response.Response>
    )
}