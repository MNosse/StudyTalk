package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.useCase.answer.GetAllAnswersByQuestionUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/studytalk/api/answers/question")
class GetAllAnswersByQuestionController(private val getAllAnswersByQuestionUseCase: GetAllAnswersByQuestionUseCase) : BaseController() {
    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.FOUND)
    @Throws(
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        QuestionNotFoundException::class
    )
    fun getAllAnswersByQuestion(
        @PathVariable id: Long,
        @RequestParam(name = "participantUid") participantUid: String
    ): Response {
        return Response(
            convert(
                getAllAnswersByQuestionUseCase.execute(
                    GetAllAnswersByQuestionUseCase.Input(
                        participantUid = participantUid,
                        questionId = id
                    )
                ).answers
            )
        )
    }

    data class Response(
        val answers: MutableList<br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response.Response>
    )
}