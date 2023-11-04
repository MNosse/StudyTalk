package br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.exception.EnrollmentRequestNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response.Response
import br.com.udesc.eso.tcc.studytalk.useCase.enrollmentRequest.GetEnrollmentRequestByParticipantIdUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/studytalk/api/enrollment-requesties/participant")
class GetEnrollmentRequestByParticipantIdController(private val getEnrollmentRequestByParticipantIdUseCase: GetEnrollmentRequestByParticipantIdUseCase) : BaseController() {
    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        EnrollmentRequestNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun getEnrollmentRequestByParticipantId(
        @PathVariable id: Long,
        @RequestParam(name = "requestingParticipantUid") requestingParticipantUid: String
    ): Response {
        return convert(
            getEnrollmentRequestByParticipantIdUseCase.execute(
                GetEnrollmentRequestByParticipantIdUseCase.Input(
                    requestingParticipantUid = requestingParticipantUid,
                    participantId = id
                )
            ).enrollmentRequest
        )
    }
}