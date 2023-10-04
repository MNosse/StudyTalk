package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.answer.UpdateAnswerUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/answers")
@Validated
class UpdateAnswerController(private val updateAnswerUseCase: UpdateAnswerUseCase) : BaseController() {
    @PutMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        AnswerIsNotFromParticipantException::class,
        AnswerNotFoundException::class
    )
    fun updateAnswer(@PathVariable id: Long, @Valid @RequestBody request: Request) {
        updateAnswerUseCase.execute(
            UpdateAnswerUseCase.Input(
                id = id,
                description = request.description,
                participantUid = request.participantUid
            )
        )
    }

    data class Request(
        @field:NotBlank(message = "A descrição deve conter ao menos um caractere.")
        @field:Size(max = 512, message = "A descrição não pode ultrapassar 512 caracteres.")
        val description: String,
        val participantUid: String
    )
}