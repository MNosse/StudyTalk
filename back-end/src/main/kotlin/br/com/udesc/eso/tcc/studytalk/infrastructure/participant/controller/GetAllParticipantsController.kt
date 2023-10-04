package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.administrator.exception.AdministratorNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.useCase.participant.GetAllParticipantsUseCase
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/participants")
class GetAllParticipantsController(private val getAllParticipantsUseCase: GetAllParticipantsUseCase) : BaseController() {
    @GetMapping("/")
    @ResponseStatus(HttpStatus.FOUND)
    @Throws(AdministratorNotFoundException::class)
    fun getAllParticipants(@RequestParam(name = "administratorUid") administratorUid: String): Response {
        return Response(
            convert(
                getAllParticipantsUseCase.execute(
                    GetAllParticipantsUseCase.Input(administratorUid = administratorUid)
                ).participants
            )
        )
    }

    data class Response(
        val participants: MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller.response.Response>
    )
}