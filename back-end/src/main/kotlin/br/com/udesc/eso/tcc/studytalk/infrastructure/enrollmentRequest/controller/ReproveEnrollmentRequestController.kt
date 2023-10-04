package br.com.udesc.eso.tcc.studytalk.infrastructure.enrollmentRequest.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.enrollmentRequest.exception.EnrollmentRequestNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPrivilegeException
import br.com.udesc.eso.tcc.studytalk.useCase.enrollmentRequest.ReproveEnrollmentRequestUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/enrollment-requesties/reprove")
class ReproveEnrollmentRequestController(private val reproveEnrollmentRequestUseCase: ReproveEnrollmentRequestUseCase) : BaseController() {
    @DeleteMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        EnrollmentRequestNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPrivilegeException::class
    )
    fun reproveEnrollmentRequestById(@PathVariable id: Long, @RequestParam(name = "reproverUid") reproverUid: String) {
        reproveEnrollmentRequestUseCase.execute(
            ReproveEnrollmentRequestUseCase.Input(
                reproverUid = reproverUid,
                id = id
            )
        )
    }
}