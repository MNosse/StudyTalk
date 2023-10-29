package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Response
import br.com.udesc.eso.tcc.studytalk.useCase.participant.DoAQuestionUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/participants")
@Validated
class DoAQuestionController(private val doAQuestionUseCase: DoAQuestionUseCase) : BaseController() {
    @PostMapping("/{participantUid}/questions/")
    @ResponseStatus(HttpStatus.CREATED)
    @Throws(ParticipantNotFoundException::class)
    fun doAQuestion(@PathVariable participantUid: String, @Valid @RequestBody request: Request): Response {
        return convert(
            doAQuestionUseCase.execute(
                DoAQuestionUseCase.Input(
                    participantUid = participantUid,
                    title = request.title,
                    description = request.description,
                    subjects = request.subjects
                )
            ).question
        )
    }

    data class Request(
        @field:NotBlank(message = "O título deve conter ao menos um caractere.")
        @field:Size(max = 256, message = "O título não pode ultrapassar 256 caracteres.")
        val title: String,
        @field:NotBlank(message = "A descrição deve conter ao menos um caractere.")
        @field:Size(max = 512, message = "A descrição não pode ultrapassar 512 caracteres.")
        val description: String,
        val subjects: MutableList<Subject>,
    )
}