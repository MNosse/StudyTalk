package br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.entity.question.exception.QuestionNotFoundException
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.question.controller.response.Response
import br.com.udesc.eso.tcc.studytalk.useCase.question.GetQuestionByIdUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/studytalk/api/questions")
class GetQuestionByIdController(private val getQuestionByIdUseCase: GetQuestionByIdUseCase) : BaseController() {
    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class,
        QuestionNotFoundException::class
    )
    fun getQuestionById(
        @PathVariable id: Long,
        @RequestParam(name = "participantUid") participantUid: String
    ): Response {
        return convert(
            getQuestionByIdUseCase.execute(
                GetQuestionByIdUseCase.Input(
                    participantUid = participantUid,
                    id = id
                )
            ).question
        )
    }
}