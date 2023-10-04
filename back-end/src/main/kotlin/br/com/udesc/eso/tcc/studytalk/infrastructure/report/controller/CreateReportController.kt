package br.com.udesc.eso.tcc.studytalk.infrastructure.report.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.report.CreateReportUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/reports")
@Validated
class CreateReportController(private val createReportUseCase: CreateReportUseCase) : BaseController() {
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @Throws(
        AnswerNotFoundException::class,
        QuestionNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class
    )
    fun createReport(@Valid @RequestBody request: Request) {
        createReportUseCase.execute(
            CreateReportUseCase.Input(
                requestingParticipantUid = request.requestingParticipantUid,
                postableId = request.postableId,
                postableType = request.postableType,
                description = request.description
            )
        )
    }

    data class Request(
        val requestingParticipantUid: String,
        val postableId: Long,
        @field:NotBlank(message = "O tipo de postagem deve ser 'ANSWER' ou 'QUESTION'")
        val postableType: String,
        @field:NotBlank(message = "A descrição deve conter ao menos um caractere.")
        @field:Size(max = 512, message = "A descrição não pode ultrapassar 512 caracteres.")
        val description: String
    )
}