package br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantWithoutPermissionException
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.converter.convert
import br.com.udesc.eso.tcc.studytalk.infrastructure.answer.controller.response.Response
import br.com.udesc.eso.tcc.studytalk.useCase.answer.GetAnswerByIdUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/answers")
class GetAnswerByIdController(private val getAnswerByIdUseCase: GetAnswerByIdUseCase) : BaseController() {
    @GetMapping("/{id}/")
    @ResponseStatus(HttpStatus.FOUND)
    @Throws(
        AnswerNotFoundException::class,
        ParticipantNotFoundException::class,
        ParticipantWithoutPermissionException::class
    )
    fun getQuestionById(
        @PathVariable id: Long,
        @RequestParam(name = "participantUid") participantUid: String
    ): Response {
        return convert(
            getAnswerByIdUseCase.execute(
                GetAnswerByIdUseCase.Input(
                    participantUid = participantUid,
                    id = id
                )
            ).answer
        )
    }
}