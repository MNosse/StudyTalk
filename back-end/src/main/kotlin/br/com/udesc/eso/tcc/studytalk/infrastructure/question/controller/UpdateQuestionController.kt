package br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller

import br.com.udesc.eso.tcc.studytalk.core.enums.Subject
import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionIsNotFromParticipantException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.question.UpdateQuestionUseCase
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/questions")
@Validated
class UpdateQuestionController(private val updateQuestionUseCase: UpdateQuestionUseCase) : BaseController() {
    @PutMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        QuestionIsNotFromParticipantException::class,
        QuestionNotFoundException::class
    )
    fun updateQuestion(@PathVariable id: Long, @Valid @RequestBody request: Request) {
        updateQuestionUseCase.execute(
            UpdateQuestionUseCase.Input(
                id = id,
                title = request.title,
                description = request.description,
                subjects = request.subjects,
                participantUid = request.participantUid
            )
        )
    }

    data class Request(
        @field:Size(max = 256, message = "O título não pode ultrapassar 256 caracteres.")
        val title: String?,
        @field:Size(max = 512, message = "A descrição não pode ultrapassar 512 caracteres.")
        val description: String?,
        val subjects: MutableList<Subject>?,
        val participantUid: String
    )
}