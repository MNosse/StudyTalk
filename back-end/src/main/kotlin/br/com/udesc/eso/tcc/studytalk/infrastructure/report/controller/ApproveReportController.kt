package br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.entity.report.exception.ReportNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.report.ApproveReportUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/reports/approve")
class ApproveReportController(private val approveReportUseCase: ApproveReportUseCase) : BaseController() {
    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        ReportNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun approveReportById(@PathVariable id: Long, @RequestParam(name = "approverUid") approverUid: String) {
        approveReportUseCase.execute(
            ApproveReportUseCase.Input(
                approverUid = approverUid,
                id = id
            )
        )
    }
}