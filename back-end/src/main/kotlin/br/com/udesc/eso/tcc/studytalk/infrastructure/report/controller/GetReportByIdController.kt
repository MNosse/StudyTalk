package br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.report.exception.ReportNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.response.Response
import br.com.udesc.eso.tcc.studytalk.useCase.report.GetReportByIdUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/studytalk/api/reports")
class GetReportByIdController(private val getReportByIdUseCase: GetReportByIdUseCase) : BaseController() {
    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.FOUND)
    @Throws(
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        ParticipantWithoutPrivilegeException::class,
        ReportNotFoundException::class
    )
    fun getReportById(@PathVariable id: Long, @RequestParam(name = "requestingParticipantUid") requestingParticipantUid: String): Response {
        return convert(
            getReportByIdUseCase.execute(
                GetReportByIdUseCase.Input(
                    requestingParticipantUid = requestingParticipantUid,
                    id = id
                )
            ).report
        )
    }
}