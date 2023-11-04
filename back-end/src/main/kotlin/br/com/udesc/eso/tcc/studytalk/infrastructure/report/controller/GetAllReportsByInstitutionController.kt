package br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.useCase.report.GetAllReportsByInstitutionUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/studytalk/api/reports/institution")
class GetAllReportsByInstitutionController(private val getAllReportsByInstitutionUseCase: GetAllReportsByInstitutionUseCase) :
    BaseController() {
    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(ParticipantNotFoundException::class, ParticipantWithoutPermissionException::class, ParticipantWithoutPrivilegeException::class)
    fun getAllReportsByInstitutionId(@PathVariable id: Long, @RequestParam(name = "requestingParticipantUid") requestingParticipantUid: String): Response {
        return Response(
            convert(
                getAllReportsByInstitutionUseCase.execute(
                    GetAllReportsByInstitutionUseCase.Input(
                        requestingParticipantUid = requestingParticipantUid,
                        id = id
                    )
                ).reports
            )
        )
    }

    data class Response(
        val reports: MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller.response.Response>
    )
}