package br.com.udesc.eso.tcc.studytalk.infrastructure.participant.controller

import br.com.udesc.eso.tcc.studytalk.core.infrastructure.controller.BaseController
import br.com.udesc.eso.tcc.studytalk.entity.answer.exception.AnswerNotFoundException
import br.com.udesc.eso.tcc.studytalk.entity.participant.exception.ParticipantNotFoundException
import br.com.udesc.eso.tcc.studytalk.useCase.participant.ChangeAnAnswerLikeStatusUseCase
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/studytalk/api/participants")
@Validated
class ChangeAnAnswerLikeStatusController(private val changeAnAnswerLikeStatusUseCase: ChangeAnAnswerLikeStatusUseCase) : BaseController() {
    @PutMapping("/{participantUid}/answers/{answerId}/change-like-status/")
    @ResponseStatus(HttpStatus.OK)
    @Throws(ParticipantNotFoundException::class, AnswerNotFoundException::class)
    fun changeAnAnswerLikeStatus(@PathVariable participantUid: String, @PathVariable answerId: Long) {
        changeAnAnswerLikeStatusUseCase.execute(
            ChangeAnAnswerLikeStatusUseCase.Input(
                participantUid = participantUid,
                answerId = answerId
            )
        )
    }
}