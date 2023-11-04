package br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.exception.EnrollmentRequestNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller.response.Response
import br.com.udesc.eso.tcc.studytalk.useCase.enrollmentRequest.GetEnrollmentRequestByIdUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/studytalk/api/enrollment-requesties")
class GetEnrollmentRequestByIdController(private val getEnrollmentRequestByIdUseCase: GetEnrollmentRequestByIdUseCase) : BaseController() {
    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        EnrollmentRequestNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun getEnrollmentRequestById(
        @PathVariable id: Long,
        @RequestParam(name = "requestingParticipantUid") requestingParticipantUid: String
    ): Response {
        return convert(
            getEnrollmentRequestByIdUseCase.execute(
                GetEnrollmentRequestByIdUseCase.Input(
                    requestingParticipantUid = requestingParticipantUid,
                    id = id
                )
            ).enrollmentRequest
        )
    }
}