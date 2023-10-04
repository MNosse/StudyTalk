package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.participant.AnswerAQuestionUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/participants")
@Validated
class AnswerAQuestionController(private val answerAQuestionUseCase: AnswerAQuestionUseCase) : BaseController() {
    @PostMapping("/{participantUid}/questions/{questionId}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    @Throws(ParticipantNotFoundException::class, QuestionNotFoundException::class)
    fun answerAQuestion(
        @PathVariable participantUid: String,
        @PathVariable questionId: Long,
        @Valid @RequestBody request: Request
    ) {
        answerAQuestionUseCase.execute(
            AnswerAQuestionUseCase.Input(
                participantUid = participantUid,
                questionId = questionId,
                description = request.description
            )
        )
    }

    data class Request(
        @field:NotBlank(message = "A descrição deve conter ao menos um caractere.")
        @field:Size(max = 512, message = "A descrição não pode ultrapassar 512 caracteres.")
        val description: String
    )
}