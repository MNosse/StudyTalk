package br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.report.exception.ReportNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.report.ReproveReportUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/reports/reprove")
class ReproveReportController(private val reproveReportUseCase: ReproveReportUseCase) : BaseController() {
    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        ReportNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun reproveReportById(@PathVariable id: Long, @RequestParam(name = "reproverUid") reproverUid: String) {
        reproveReportUseCase.execute(
            ReproveReportUseCase.Input(
                reproverUid = reproverUid,
                id = id
            )
        )
    }
}