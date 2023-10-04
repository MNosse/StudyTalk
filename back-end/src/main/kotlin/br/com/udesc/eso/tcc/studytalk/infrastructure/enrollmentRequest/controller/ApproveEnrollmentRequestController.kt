package br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.exception.EnrollmentRequestNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.useCase.enrollmentRequest.ApproveEnrollmentRequestUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/enrollment-requesties/approve")
class ApproveEnrollmentRequestController(private val approveEnrollmentRequestUseCase: ApproveEnrollmentRequestUseCase) : BaseController() {
    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        EnrollmentRequestNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun approveEnrollmentRequestById(@PathVariable id: Long, @RequestParam(name = "approverUid") approverUid: String) {
        approveEnrollmentRequestUseCase.execute(
            ApproveEnrollmentRequestUseCase.Input(
                approverUid = approverUid,
                id = id
            )
        )
    }
}