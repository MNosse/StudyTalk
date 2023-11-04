package br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.useCase.enrollmentRequest.GetAllEnrollmentRequestByInstitutionUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/studytalk/api/enrollment-requesties/institution")
class GetAllEnrollmentRequestiesByInstitutionController(private val getAllEnrollmentRequestByInstitutionUseCase: GetAllEnrollmentRequestByInstitutionUseCase) : BaseController() {
    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        ParticipantNotFoundException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun getAllEnrollmentRequestiesByInstitutionId(
        @PathVariable id: Long,
        @RequestParam(name = "requestingParticipantUid") requestingParticipantUid: String
    ): Response {
        return Response(
            convert(
                getAllEnrollmentRequestByInstitutionUseCase.execute(
                    GetAllEnrollmentRequestByInstitutionUseCase.Input(
                        requestingParticipantUid = requestingParticipantUid,
                        institutionId = id
                    )
                ).enrollmentRequesties
            )
        )
    }

    data class Response(
        val enrollmentRequesties: MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response.Response>
    )
}